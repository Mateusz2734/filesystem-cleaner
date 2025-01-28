export type GroupInfo = {
  reason: string;
  files: FileInfo[];
};

export type FileInfo = {
  path: string;
  size: number;
  modifiedAt: string;
  createdAt: string;
};
