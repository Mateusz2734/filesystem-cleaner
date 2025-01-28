import { Popover, PopoverTrigger, PopoverContent } from "@/components/ui/popover";
import { Badge } from "@/components/ui/badge";
import { Input } from "@/components/ui/input";

export function FileFinder() {
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