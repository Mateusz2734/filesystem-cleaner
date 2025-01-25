import {
    ColumnDef,
    flexRender,
    getCoreRowModel,
    getPaginationRowModel,
    RowSelectionState,
    useReactTable,
} from "@tanstack/react-table";
import * as React from "react";

import { Button } from "@/components/ui/button";
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from "@/components/ui/table";
import { Badge } from "./ui/badge";
import { FileInfo } from "./data-table-columns";

interface DataTableProps<TValue> {
    columns: ColumnDef<FileInfo, TValue>[];
    data: FileInfo[];
    reason: string;
}

export function DataTable<TValue>({
    columns,
    data: initialData,
    reason,
}: DataTableProps<TValue>) {
    const [data, setData] = React.useState<FileInfo[]>(initialData);
    const [rowSelection, setRowSelection] = React.useState<RowSelectionState>({});

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

    const handleRemoveSelected = () => {
        const selectedIds = Object.keys(rowSelection);

        if (!selectedIds.length) {
            return;
        }
        const newData = data.filter((_, index) => !selectedIds.includes(index.toString()));
        const toRemove = data.filter((_, index) => selectedIds.includes(index.toString())).map((item) => item.path);

        fetch('/api/file/delete', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(toRemove),
        }).then((response) => console.log(response)).catch(() => { });

        setData(newData);
        setRowSelection({});
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
                <Button onClick={handleRemoveSelected} variant="destructive">Remove</Button>
                <Button variant="secondary">Move</Button>
                <Button variant="outline">Archive</Button>
            </div>
        </div>
    );
}