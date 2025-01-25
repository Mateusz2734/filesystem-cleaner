import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { BrowserRouter, Routes, Route } from "react-router";
import { Toaster } from "sonner";

import { Layout } from '@/app/layout.tsx';
import { MainPage } from '@/app/pages/MainPage';
import { LogPage } from '@/app/pages/LogPage';
import { TitledPage } from '@/app/pages/TitledPage';
import { GroupsPage } from '@/app/pages/GroupsPage';

import './index.css';

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <BrowserRouter>
      <Routes>
        <Route element={<Layout />}>
          <Route index element={<MainPage />} />
          <Route path="/logs" element={<LogPage />} />
          <Route path="/groups" element={<GroupsPage />} />
          <Route path="*" element={<TitledPage />} />
        </Route>
      </Routes>
      <Toaster richColors theme="dark" closeButton />
    </BrowserRouter>
  </StrictMode >,
);
