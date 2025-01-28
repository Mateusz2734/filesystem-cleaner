import { clsx, type ClassValue } from "clsx";
import { twMerge } from "tailwind-merge";

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

interface NavItem {
  title: string;
  url: string;
}

interface NavGroup {
  title: string;
  items: NavItem[];
  later?: boolean;
}

export const data: NavGroup[] = [
  {
    title: "Getting Started",
    items: [
      {
        title: "Start Indexing",
        url: "/",
      },
      {
        title: "Check Action Logs",
        url: "/logs",
      },
    ],
  },
  {
    title: "After Indexing",
    later: true,
    items: [
      {
        title: "Manage Groups",
        url: "/groups",
      },
      {
        title: "Search by Keyword",
        url: "/search",
      },
    ],
  },
];

export const urlToTitle = (url: string): string => {
  const item = data
    .flatMap((group) => group.items)
    .find((item) => item.url === url);
  return item?.title || url;
};
