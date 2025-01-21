import { zodResolver } from "@hookform/resolvers/zod";
import React, { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { toast } from "sonner";
import { z } from "zod";

import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Form, FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";
import { Alert, AlertTitle } from "@/components/ui/alert";

import { useIndexingStore } from "@/hooks/store";


// Utility to check if a path is absolute
function isAbsolutePath(path: string): boolean {
    const windowsPattern = /^[a-zA-Z]:\\/;
    const unixPattern = /^\//;

    return windowsPattern.test(path) || unixPattern.test(path);
};

// Utility to check if a path is likely a directory
function isDirectoryPath(path: string): boolean {
    return !/\.[a-zA-Z0-9]+$/.test(path);
};


function FileFinder() {
    return (
        <Popover>
            <PopoverTrigger>
                <Badge className="cursor-pointer ml-4">Help</Badge>
            </PopoverTrigger>
            <PopoverContent className="w-96 p-4 flex flex-col gap-4">
                <p className="text-sm text-gray-400">
                    Due to browser security restrictions, you must manually type or paste path of the root directory to index. This file input is provided solely to assist you in copying the path.
                </p>
                <Input type="file" />
            </PopoverContent>
        </Popover>
    );
}


const formSchema = z.object({
    root: z.string().min(2, {
        message: "Directory root path must be at least 2 characters.",
    }).refine(
        (value) => isAbsolutePath(value),
        { "message": "Directory root path must be an absolute path." }
    ).refine(
        (value) => isDirectoryPath(value),
        { "message": "Directory root path must be a directory." }
    ),
});

type FormValues = z.infer<typeof formSchema>;

export function FileForm() {
    const { currentRoot, setCurrentRoot, isIndexing, startIndexing } = useIndexingStore();

    const form = useForm<FormValues>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            root: "",
        },
    });

    function onSubmit(values: z.infer<typeof formSchema>) {
        const performIndexing = async () => {
            try {
                const response = await fetch('/api/index/start', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(values),
                });

                const json = await response.json();

                if (!response.ok) {
                    form.setError('root', {
                        type: 'server',
                        message: json.error || 'Failed to start indexing',
                    });
                }

                startIndexing();
                toast.message("Indexing started!", { description: "Please, do not refresh the browser tab until indexing is complete.", duration: 10000 });
            } catch (error) {
                console.error('Error starting indexing:', error);
            } finally {
                form.reset();
            }
        };

        if (values.root) {
            performIndexing();
            setCurrentRoot(values.root);
        } else {
            form.setError('root', {
                type: 'manual',
                message: 'Directory root path is required.',
            });
        }
    }

    return (
        <Card className="w-[30em]">
            <CardHeader>
                <CardTitle>Start Indexing</CardTitle>
                <CardDescription>Index a directory to get more insights.</CardDescription>
            </CardHeader>
            <CardContent>
                <Form {...form} >
                    <form className="flex flex-col w-full" onSubmit={form.handleSubmit(onSubmit)}>
                        <FormField
                            control={form.control}
                            name="root"
                            render={({ field }) => (
                                <FormItem>
                                    <FormLabel>
                                        Root Directory
                                        <FileFinder />
                                    </FormLabel>
                                    <FormControl>
                                        <Input placeholder={currentRoot}  {...field} />
                                    </FormControl>
                                    <FormMessage />
                                    <FormDescription>This is  the root path of indexed directory.</FormDescription>
                                </FormItem>
                            )}
                        />
                        <Button type="submit" className="w-[70%] self-center mt-4" disabled={isIndexing}>Start Indexing</Button>
                    </form>
                </Form>
            </CardContent>
        </Card>
    );
}

export const IndexingStatus: React.FC = () => {
    const [statusHistory, setStatusHistory] = useState<string[]>([]);
    const { endIndexing, isIndexing, indexingDone } = useIndexingStore();

    useEffect(() => {
        if (!isIndexing) {
            return;
        }

        const eventSource = new EventSource('/api/index/progress');

        eventSource.onmessage = (event) => {
            if (event.data === 'END') {
                eventSource.close();
                endIndexing("success");
            } else {
                setStatusHistory((prev) => [event.data, ...prev]);
            }
        };

        eventSource.onerror = (error) => {
            console.error('Error receiving updates:', error);
            eventSource.close();
            endIndexing("failure");
        };

        return () => {
            eventSource.close();
        };
    }, [endIndexing, isIndexing, indexingDone]);

    return (
        <Card className="w-[30em] mt-4">
            <CardHeader>
                <CardTitle>Latest Indexing History</CardTitle>
            </CardHeader>
            <CardContent>
                {statusHistory.length === 0 && <Alert >
                    <AlertTitle>No indexing history yet.</AlertTitle>
                </Alert>}

                {statusHistory.slice(0, 5).map((status, index) => (
                    <Alert className="mb-2" key={index}><AlertTitle className="p-0">{status}</AlertTitle></Alert>
                ))}
            </CardContent>
        </Card>
    );
};

export function MainPage() {
    return (
        <>
            <FileForm />
            <IndexingStatus />
        </>
    );
}


