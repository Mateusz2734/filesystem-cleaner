// import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { BrowserRouter, Routes, Route } from "react-router";
import { Toaster } from "sonner";

import { Layout } from '@/app/layout.tsx';
import { MainPage } from '@/app/pages/MainPage';
import { LogPage } from '@/app/pages/LogPage';
import { NotFoundPage } from '@/app/pages/NotFoundPage';
import { GroupsPage } from '@/app/pages/GroupsPage';
import { SearchPage } from '@/app/pages/SearchPage';

import './index.css';

createRoot(document.getElementById('root')!).render(
  // <StrictMode>
  <BrowserRouter>
    <Routes>
      <Route element={<Layout />}>
        <Route index element={<MainPage />} />
        <Route path="/logs" element={<LogPage />} />
        <Route path="/groups" element={<GroupsPage />} />
        <Route path="/search" element={<SearchPage />} />
        <Route path="*" element={<NotFoundPage />} />
      </Route>
    </Routes>
    <Toaster richColors theme="dark" closeButton />
  </BrowserRouter>
  // </StrictMode >,
);
