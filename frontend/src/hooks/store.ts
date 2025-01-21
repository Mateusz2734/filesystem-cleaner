import { create } from "zustand";
import { createJSONStorage, persist } from "zustand/middleware";

interface IndexingState {
  isIndexing: boolean;
  setIsIndexing: (isIndexing: boolean) => void;

  indexingDone: boolean;
  setIndexingDone: (indexingDone: boolean) => void;

  endIndexing: (result: IndexingResult) => void;
  startIndexing: () => void;

  currentRoot: string;
  setCurrentRoot: (currentRoot: string) => void;

  previouslyIndexed: Set<string>;
  refreshPreviouslyIndexed: () => void;
}

type IndexingResult = "success" | "failure" | "neutral";

type SerializedSet = { __type: "Set"; value: string[] };

function isSerializedSet(obj: unknown): obj is SerializedSet {
  return (
    typeof obj === "object" &&
    obj !== null &&
    "__type" in obj &&
    (obj as SerializedSet).__type === "Set" &&
    Array.isArray((obj as SerializedSet).value)
  );
}

export const useIndexingStore = create<IndexingState>()(
  persist(
    (set, get) => ({
      isIndexing: false,
      setIsIndexing: (isIndexing) => set({ isIndexing }),

      indexingDone: false,
      setIndexingDone: (indexingDone) => set({ indexingDone }),

      currentRoot: "",
      setCurrentRoot: (newRoot) => set({ currentRoot: newRoot }),

      endIndexing: (result) => {
        set({ isIndexing: false, indexingDone: true });
        if (result === "success") {
          get().refreshPreviouslyIndexed();
        } else if (result === "failure") {
          set({ currentRoot: "" });
        }
      },
      startIndexing: () =>
        set(() => ({
          isIndexing: true,
          indexingDone: false,
        })),

      previouslyIndexed: new Set(),
      refreshPreviouslyIndexed: () =>
        set((state) => ({
          previouslyIndexed: new Set([
            ...state.previouslyIndexed,
            state.currentRoot,
          ]),
        })),
    }),
    {
      name: "indexing-store",
      storage: createJSONStorage(() => sessionStorage, {
        replacer: (_, value) => {
          if (value instanceof Set) {
            return { __type: "Set", value: Array.from(value) };
          }
          return value;
        },
        reviver: (_, value) => {
          if (isSerializedSet(value)) {
            return new Set(value.value);
          }
          return value;
        },
      }),
    }
  )
);
