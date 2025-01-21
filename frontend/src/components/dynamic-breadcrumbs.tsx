import { useLocation } from 'react-router';

import {
    Breadcrumb,
    BreadcrumbItem,
    BreadcrumbLink,
    BreadcrumbList,
    BreadcrumbPage,
    BreadcrumbSeparator,
} from "@/components/ui/breadcrumb";

import { urlToTitle } from '@/lib/utils';

interface BreadcrumbItemProps {
    label: string;
    url: string;
    isLast: boolean;
}

interface BreadcrumbData {
    label: string;
    url: string;
    isLast: boolean;
}

const createBreadcrumbItem = ({ label, url, isLast }: BreadcrumbItemProps) => (
    <BreadcrumbItem key={url}>
        {isLast ? (
            <BreadcrumbPage>{label}</BreadcrumbPage>
        ) : (
            <BreadcrumbLink href={url}>{label}</BreadcrumbLink>
        )}
    </BreadcrumbItem>
);

const generateItemData = (pathSegments: string[]): BreadcrumbData[] => {
    return pathSegments.map((segment, index) => {
        const url = `/${pathSegments.slice(0, index + 1).join('/')}`;
        return { label: urlToTitle(`/${segment}`), url, isLast: index === pathSegments.length - 1 };
    });
};

export function DynamicBreadcrumbs() {
    const location = useLocation();
    const breadcrumbData = generateItemData(location.pathname.split('/').filter(Boolean));

    const renderBreadcrumbs = () => {
        if (location.pathname === '/') {
            return (
                <BreadcrumbItem>
                    <BreadcrumbPage>Home</BreadcrumbPage>
                </BreadcrumbItem>
            );
        }

        return (
            <>
                <BreadcrumbItem>
                    <BreadcrumbLink href="/">Home</BreadcrumbLink>
                </BreadcrumbItem>
                <BreadcrumbSeparator className="hidden md:block" />
                {breadcrumbData.map(createBreadcrumbItem)}
            </>
        );
    };

    return (
        <Breadcrumb className="space-x-2">
            <BreadcrumbList>
                {renderBreadcrumbs()}
            </BreadcrumbList>
        </Breadcrumb>
    );
};
