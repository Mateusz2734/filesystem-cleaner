import { useEffect, useState } from "react";

import { columns } from "@/components/data-table-columns";
import { DataTable } from "@/components/data-table";
import { useIndexingStore } from "@/hooks/store";

import { GroupInfo } from "@/app/model";


export function GroupsPage() {
  const [groups, setGroups] = useState<GroupInfo[]>([]);
  const { currentRoot } = useIndexingStore();

  useEffect(() => {
    const fetchGroups = async () => {
      try {
        const response = await fetch(`/api/file/groups?rootPath=${encodeURIComponent(currentRoot)}`);

        if (!response.ok) {
          return;
        }

        const data: GroupInfo[] = await response.json();
        setGroups(data);
      } catch (error) {
        console.error('Error fetching groups:', error);
      }
    };

    if (groups.length === 0) {
      fetchGroups();
    }
  }, [currentRoot, groups.length]);

  if (groups.length === 0) {
    return <div>Loading...</div>;
  }

  return (
    <div className="w-[80%] space-y-10">
      {groups.length !== 0 && groups.map((group, index) => {
        const filteredFiles = group.files.filter((file) => file.path.endsWith('txt'));
        console.log(filteredFiles);
        return <DataTable key={index} columns={columns} data={filteredFiles} reason={group.reason} />;
      })}
    </div>
  );
}