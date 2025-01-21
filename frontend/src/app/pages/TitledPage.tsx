import { useLocation } from "react-router";

export function TitledPage() {
    const location = useLocation();


    return (
        <div>
            <h1 className="text-2xl font-bold">Titled Page</h1>
            <h1>{location.pathname}</h1>
        </div>
    );
}