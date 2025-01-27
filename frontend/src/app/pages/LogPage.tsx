import { ArrowRight } from 'lucide-react';
import React, { useEffect } from 'react';

import { Alert } from '@/components/ui/alert';
import { Badge } from '@/components/ui/badge';
import { Card, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from '@/components/ui/tooltip';

interface ActionLogItem {
    datetime: string;
    method: string;
    file1: string;
    file2: string;
}

interface ActionLogProps {
    logs: ActionLogItem[];
}

function FileNameBadge({ file }: { file: string; }) {
    return (
        <TooltipProvider>
            <Tooltip>
                <TooltipContent>
                    {file}
                </TooltipContent>
                <TooltipTrigger asChild>
                    <Badge variant="secondary" className="text-center truncate max-w-[100%]">{file}</Badge>
                </TooltipTrigger>
            </Tooltip>
        </TooltipProvider>
    );
}

const LogItem: React.FC<ActionLogItem> = ({ datetime, method, file1, file2 }: ActionLogItem) => (
    <Alert className="w-[90%] shadow-lg p-1">
        <div className="flex justify-between">
            <div className="w-[16.66%] flex justifyflex-start">
                <Badge variant="secondary" className="text-gray-500 p-0.5 italic">{datetime}</Badge>
            </div>
            <div className='w-[16.66%] flex justify-center'>
                <Badge className="font-bold">{method}</Badge>
            </div>
            <div className="w-[25%] flex justify-center">
                <FileNameBadge file={file1} />
            </div>
            <div className='w-[41.66%] flex'>
                {method !== 'DELETE' && <>
                    <div className="w-[25%] flex justify-center items-center">
                        <ArrowRight size="16" />
                    </div>
                    <div className="w-[75%] flex justify-center">
                        <FileNameBadge file={file2} />
                    </div>
                </>}
            </div>

        </div>
    </Alert>
);

const ActionLog: React.FC<ActionLogProps> = ({ logs }) => {
    return (<>
        {logs.map((log, index) => (
            <LogItem key={index} {...log} />
        ))}
    </>
    );
};


export function LogPage() {
    const [logs, setLogs] = React.useState<ActionLogItem[]>([]);

    useEffect(() => {
        const fetchLogs = async () => {
            const response = await fetch('/api/log');

            if (response.status !== 200) {
                return;
            }
            const data: ActionLogItem[] = await response.json();

            setLogs(data);

        };

        fetchLogs();
    }, []);

    return (
        <div className="flex flex-col items-center justify-center w-full space-y-2">
            {
                logs.length === 0
                    ? <Card className='w-[30em]'><CardHeader>
                        <CardTitle className=''>
                            No logs!
                        </CardTitle>
                        <CardDescription>
                            Start performing actions to see logs here.
                        </CardDescription>
                    </CardHeader>
                    </Card>
                    : <ActionLog logs={logs} />
            }
        </div>
    );
}