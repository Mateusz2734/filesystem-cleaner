import {
    ColumnDef,
    flexRender,
    getCoreRowModel,
    getPaginationRowModel,
    RowSelectionState,
    useReactTable,
} from "@tanstack/react-table";
import * as React from "react";

import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from "@/components/ui/table";

import { FileInfo } from "@/app/model";
import { useIndexingStore } from "@/hooks/store";

interface DataTableProps<TValue> {
    columns: ColumnDef<FileInfo, TValue>[];
    data: FileInfo[];
    reason: string;
}

interface FileOperationProps {
    filenames: string[];
    destination?: string;
}

// Modify functions to use the interface
function deleteFiles(props: FileOperationProps) {
    return fetch('/api/file/delete', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(props.filenames),
    })
        .then((response) => {
            if (!response.ok) {
                throw new Error('Delete operation failed');
            }
            return response.json();
        })
        .catch((error) => {
            console.error('Error deleting files:', error);
            throw error;
        });
}

function moveFiles(props: FileOperationProps) {
    if (!props.destination) {
        throw new Error('Destination is required for move operation');
    }

    return fetch('/api/file/move', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            files: props.filenames,
            destination: props.destination
        }),
    })
        .then((response) => {
            if (!response.ok) {
                throw new Error('Move operation failed');
            }
            return response.json();
        })
        .catch((error) => {
            console.error('Error moving files:', error);
            throw error;
        });
}

function archiveFiles(props: FileOperationProps) {
    if (!props.destination) {
        throw new Error('Destination is required for archive operation');
    }

    return fetch('/api/file/archive', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            files: props.filenames,
            destination: props.destination
        }),
    })
        .then((response) => {
            if (!response.ok) {
                throw new Error('Archive operation failed');
            }
            return response.json();
        })
        .catch((error) => {
            console.error('Error archiving files:', error);
            throw error;
        });
}

export function DataTable<TValue>({
    columns,
    data: initialData,
    reason,
}: DataTableProps<TValue>) {
    const [data, setData] = React.useState<FileInfo[]>(initialData);
    const [rowSelection, setRowSelection] = React.useState<RowSelectionState>({});
    const { currentRoot } = useIndexingStore();

    const table = useReactTable({
        data,
        columns,
        getCoreRowModel: getCoreRowModel(),
        getPaginationRowModel: getPaginationRowModel(),
        onRowSelectionChange: setRowSelection,
        state: {
            rowSelection,
        },
    });

    if (data.length < 2) {
        return null;
    }

    const handleFiles = <T extends (props: FileOperationProps) => Promise<unknown>>(
        fn: T,
        destination?: string
    ) => {
        const selectedIds = Object.keys(rowSelection);

        if (!selectedIds.length) {
            return;
        }

        const newData = data.filter((_, index) => !selectedIds.includes(index.toString()));
        const toBeWorked = data.filter((_, index) => selectedIds.includes(index.toString())).map((item) => item.path);

        // Prepare operation props
        const operationProps: FileOperationProps = {
            filenames: toBeWorked,
            ...(destination && { destination })
        };

        fn(operationProps)
            .then(() => {
                setData(newData);
                setRowSelection({});
            })
            .catch((error) => {
                console.error('Operation failed:', error);
            });
    };

    return (
        <div>
            <Badge className="mb-4 bg-yellow-100 text-yellow-800 hover:bg-yellow-100 hover:text-yellow-800" variant="secondary">
                {reason}
            </Badge>
            <div className="rounded-md border">
                <Table>
                    <TableHeader>
                        {table.getHeaderGroups().map((headerGroup) => (
                            <TableRow key={headerGroup.id}>
                                {headerGroup.headers.map((header) => {
                                    return (
                                        <TableHead key={header.id}>
                                            {header.isPlaceholder
                                                ? null
                                                : flexRender(
                                                    header.column.columnDef.header,
                                                    header.getContext()
                                                )}
                                        </TableHead>
                                    );
                                })}
                            </TableRow>
                        ))}
                    </TableHeader>
                    <TableBody>
                        {table.getRowModel().rows?.length ? (
                            table.getRowModel().rows.map((row) => (
                                <TableRow
                                    key={row.id}
                                    data-state={row.getIsSelected() && "selected"}
                                >
                                    {row.getVisibleCells().map((cell) => (
                                        <TableCell key={cell.id}>
                                            {flexRender(cell.column.columnDef.cell, cell.getContext())}
                                        </TableCell>
                                    ))}
                                </TableRow>
                            ))
                        ) : (
                            <TableRow>
                                <TableCell colSpan={columns.length} className="h-24 text-center">
                                    No results.
                                </TableCell>
                            </TableRow>
                        )}
                    </TableBody>
                </Table>
            </div>
            <div className="flex justify-end mt-4 gap-4 ">
                <Button onClick={() => handleFiles(deleteFiles)} variant="destructive">Remove</Button>
                <Button onClick={() => handleFiles(moveFiles, currentRoot)} variant="secondary">Move</Button>
                <Button onClick={() => handleFiles(archiveFiles, currentRoot)} variant="outline">Archive</Button>
            </div>
        </div>
    );
}