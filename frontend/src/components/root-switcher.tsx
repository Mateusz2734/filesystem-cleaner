import { Check, ChevronsUpDown, GalleryVerticalEnd } from "lucide-react";

import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import {
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
} from "@/components/ui/sidebar";
import {
  TooltipProvider,
  Tooltip,
  TooltipTrigger,
  TooltipContent
} from "@/components/ui/tooltip";

interface RootSwitcherProps {
  roots: Set<string>;
  defaultRoot: string;
  setSelectedRoot: (version: string) => void;
}

export function RootSwitcher({
  roots,
  defaultRoot,
  setSelectedRoot,
}: RootSwitcherProps) {
  return (
    <TooltipProvider>
      <SidebarMenu>
        <SidebarMenuItem>
          <DropdownMenu>
            <Tooltip>
              {defaultRoot && (
                <TooltipContent>
                  <span className="text-xs">{defaultRoot}</span>
                </TooltipContent>
              )}
              <TooltipTrigger asChild>
                <DropdownMenuTrigger asChild>
                  <SidebarMenuButton
                    size="lg"
                    className="data-[state=open]:bg-sidebar-accent data-[state=open]:text-sidebar-accent-foreground mt-2"
                  >
                    <div className="flex aspect-square size-8 items-center justify-center rounded-lg bg-sidebar-primary text-sidebar-primary-foreground">
                      <GalleryVerticalEnd className="size-4" />
                    </div>
                    <div className="flex flex-col gap-0.5 leading-none">
                      <span className="font-semibold">Current Root</span>
                      <span className="text-xs line-clamp-3 text-nowrap  w-[10rem]">{defaultRoot ? defaultRoot : "No current root."}</span>
                    </div>
                    <ChevronsUpDown className="ml-auto" />
                  </SidebarMenuButton>
                </DropdownMenuTrigger>
              </TooltipTrigger>
            </Tooltip>
            {roots.size > 1 && (
              <DropdownMenuContent align="start">
                {Array.from(roots).map((root) => (
                  <DropdownMenuItem
                    key={root}
                    onSelect={() => setSelectedRoot(root)}
                  >
                    {root}
                    {root === defaultRoot && <Check className="ml-auto" />}
                  </DropdownMenuItem>
                ))}
              </DropdownMenuContent>
            )}
          </DropdownMenu>
        </SidebarMenuItem>
      </SidebarMenu>
    </TooltipProvider>
  );
}
