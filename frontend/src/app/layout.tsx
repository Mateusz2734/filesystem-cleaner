import { Outlet } from 'react-router';

import { AppSidebar } from "@/components/app-sidebar";
import { DynamicBreadcrumbs } from '@/components/dynamic-breadcrumbs';
import { Separator } from "@/components/ui/separator";
import { SidebarInset, SidebarProvider, SidebarTrigger } from "@/components/ui/sidebar";

export function Layout() {
  return (
    <SidebarProvider>
      <AppSidebar />
      <SidebarInset>
        <header className="flex h-16 shrink-0 items-center gap-2 border-b px-4">
          <SidebarTrigger className="-ml-1" />
          <Separator orientation="vertical" className="mr-2 h-4" />
          <DynamicBreadcrumbs />
        </header>
        <main className="p-4 flex flex-col w-full h-full items-center">
          <Outlet />
        </main>
      </SidebarInset>
    </SidebarProvider >
  );
}
