import { zodResolver } from '@hookform/resolvers/zod';
import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { z } from 'zod';

import { Button } from '@/components/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form';
import { Alert, AlertTitle } from '@/components/ui/alert';
import { Input } from '@/components/ui/input';

import { useIndexingStore } from '@/hooks/store';
import { FileInfo } from '@/app/model';

const formSchema = z.object({
    query: z.string().min(2, {
        message: "Directory root path must be at least 2 characters.",
    })
});

type FormValues = z.infer<typeof formSchema>;

interface QueryFormProps {
    setResults: React.Dispatch<React.SetStateAction<FileInfo[]>>;
}
export function QueryForm({ setResults }: QueryFormProps) {
    const { currentRoot } = useIndexingStore();

    const form = useForm<FormValues>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            query: "",
        },
    });

    function onSubmit({ query }: FormValues) {
        const search = async () => {
            try {
                const response = await fetch(`/api/file/search?rootPath=${encodeURIComponent(currentRoot)}&query=${encodeURIComponent(query)}`);

                const json = await response.json();

                if (!response.ok) {
                    form.setError('root', {
                        type: 'server',
                        message: json.error || 'Failed to search',
                    });
                    return;
                }

                setResults(json);
            } catch (error) {
                console.error('Error searching:', error);
            }
        };

        if (query) {
            search();
        } else {
            form.setError('query', {
                type: 'manual',
                message: 'Query string is required.',
            });
        }
    }

    return (
        <Card className="w-[30em]">
            <CardHeader>
                <CardTitle>Search by given keyword/query</CardTitle>
                <CardDescription>Get files most similar to the given query.</CardDescription>
            </CardHeader>
            <CardContent>
                <Form {...form} >
                    <form className="flex flex-col w-full" onSubmit={form.handleSubmit(onSubmit)}>
                        <FormField
                            control={form.control}
                            name="query"
                            render={({ field }) => (
                                <FormItem>
                                    <FormLabel>
                                        Search Query
                                    </FormLabel>
                                    <FormControl>
                                        <Input placeholder="E.g. Bananas"  {...field} />
                                    </FormControl>
                                    <FormMessage />
                                </FormItem>
                            )}
                        />
                        <Button type="submit" className="w-[70%] self-center mt-4">Search</Button>
                    </form>
                </Form>
            </CardContent>
        </Card>
    );
}

function SearchResults({ results }: { results: FileInfo[]; }) {
    return (
        <Card className="w-[30em] mt-4">
            <CardHeader>
                <CardTitle>Most similar files</CardTitle>
            </CardHeader>
            <CardContent>
                {results.length === 0 && <Alert >
                    <AlertTitle>No indexing history yet.</AlertTitle>
                </Alert>}

                {results.slice(0, 5).map(({ path }, index) => (
                    <Alert className="mb-2" key={index}><AlertTitle className="p-0">{path}</AlertTitle></Alert>
                ))}
            </CardContent>
        </Card>);
}

export function SearchPage() {
    const [searchResults, setSearchResults] = useState<FileInfo[]>([]);

    return (
        <>
            <QueryForm setResults={setSearchResults} />
            {searchResults && searchResults.length !== 0 && <SearchResults results={searchResults} />}
        </>
    );
}