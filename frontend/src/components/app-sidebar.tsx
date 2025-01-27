import { useLocation, useNavigate } from "react-router";

import {
  Sidebar,
  SidebarContent,
  SidebarGroup,
  SidebarGroupContent,
  SidebarGroupLabel,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
  SidebarRail,
} from "@/components/ui/sidebar";
import { RootSwitcher } from "@/components/root-switcher";

import { useIndexingStore } from "@/hooks/store";
import { data } from "@/lib/utils";

export function AppSidebar({ ...props }: React.ComponentProps<typeof Sidebar>) {
  const currentLocation = useLocation().pathname;
  const navigate = useNavigate();
  const { isIndexing, indexingDone, currentRoot, previouslyIndexed, setCurrentRoot } = useIndexingStore();

  return (
    <Sidebar {...props}>
      <SidebarContent>
        <RootSwitcher roots={previouslyIndexed} defaultRoot={currentRoot} setSelectedRoot={setCurrentRoot} />
        {/* We create a SidebarGroup for each parent. */}
        {data.map((group) => (
          <SidebarGroup key={group.title}>
            <SidebarGroupLabel>{group.title}</SidebarGroupLabel>
            <SidebarGroupContent>
              <SidebarMenu>
                {group.items.map((item) => (
                  <SidebarMenuItem key={item.title}>
                    <SidebarMenuButton onClick={() => navigate(item.url)} isActive={currentLocation === item.url} disabled={group.later && ((!indexingDone || isIndexing) || !currentRoot)}>
                      {item.title}
                    </SidebarMenuButton>
                  </SidebarMenuItem>
                ))}
              </SidebarMenu>
            </SidebarGroupContent>
          </SidebarGroup>
        ))}
      </SidebarContent>
      <SidebarRail />
    </Sidebar>
  );
}
