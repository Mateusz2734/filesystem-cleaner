import { ColumnDef } from "@tanstack/react-table";
import { Checkbox } from "@/components/ui/checkbox";

export type FileInfo = {
    path: string;
    size: number;
    modifiedAt: string;
    createdAt: string;
};

function formatFileSize(bytes: number): string {
    if (bytes === 0) return "0 B";
    const sizes = ["B", "KiB", "MiB", "GiB"];
    const i = Math.floor(Math.log(bytes) / Math.log(1024));
    const size = bytes / Math.pow(1024, i);
    return `${size.toFixed(2)} ${sizes[i]}`;
}

export const columns: ColumnDef<FileInfo>[] = [
    {
        id: "select",
        header: ({ table }) => (
            <Checkbox
                checked={
                    table.getIsAllPageRowsSelected() ||
                    (table.getIsSomePageRowsSelected() && "indeterminate")
                }
                onCheckedChange={(value) => table.toggleAllPageRowsSelected(!!value)}
                aria-label="Select all"
            />
        ),
        cell: ({ row }) => (
            <Checkbox
                checked={row.getIsSelected()}
                onCheckedChange={(value) => row.toggleSelected(!!value)}
                aria-label="Select row"
            />
        ),
        enableSorting: false,
        enableHiding: false,
    },
    {
        accessorKey: "path",
        header: "Path",
        cell: ({ row }) => <div className="truncate">{row.getValue("path")}</div>,
    },
    {
        accessorKey: "size",
        header: () => <div className="text-center">Size</div>,
        cell: ({ row }) => {
            const size = row.getValue("size") as number;

            return <div className="text-center font-medium">{formatFileSize(size)}</div>;
        },
    },
    {
        accessorKey: "modifiedAt",
        header: "Modified At",
        cell: ({ row }) => {
            const date = new Date(row.getValue("modifiedAt"));
            return <div>{date.toLocaleString()}</div>;
        },
    },
    {
        accessorKey: "createdAt",
        header: "Created At",
        cell: ({ row }) => {
            const date = new Date(row.getValue("createdAt"));
            return <div>{date.toLocaleString()}</div>;
        },
    },
];


export type Payment = {
    id: string;
    amount: number;
    status: "pending" | "processing" | "success" | "failed";
    email: string;
};


// export const columns: ColumnDef<Payment>[] = [
//     {
//         id: "select",
//         header: ({ table }) => (
//             <Checkbox
//                 checked={
//                     table.getIsAllPageRowsSelected() ||
//                     (table.getIsSomePageRowsSelected() && "indeterminate")
//                 }
//                 onCheckedChange={(value) => table.toggleAllPageRowsSelected(!!value)}
//                 aria-label="Select all"
//             />
//         ),
//         cell: ({ row }) => (
//             <Checkbox
//                 checked={row.getIsSelected()}
//                 onCheckedChange={(value) => row.toggleSelected(!!value)}
//                 aria-label="Select row"
//             />
//         ),
//         enableSorting: false,
//         enableHiding: false,
//     },
//     {
//         accessorKey: "status",
//         header: "Status",
//     },
//     {
//         accessorKey: "email",
//         header: "Header"
//     },
//     {
//         accessorKey: "amount",
//         header: () => <div className="text-center">Amount</div>,
//         cell: ({ row }) => {
//             const amount = parseFloat(row.getValue("amount"));
//             const formatted = new Intl.NumberFormat("en-US", {
//                 style: "currency",
//                 currency: "USD",
//             }).format(amount);

//             return <div className="text-center font-medium">{formatted}</div>;
//         },
//     },
// ];