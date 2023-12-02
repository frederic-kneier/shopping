import {createHashRouter, createRoutesFromElements, Navigate, Outlet, Route, RouterProvider} from "react-router-dom";
import ShoppingListTableRoute from "./ShoppingListTableRoute.tsx";
import ShoppingListItemRoute from "./ShoppingListItemRoute.tsx";
import NoMatch from "./NoMatch.tsx";
import {useAuth} from "oidc-react";
import ApplicationFrame from "../components/ApplicationFrame.tsx";

export default function Routing() {
    const auth = useAuth()
    const router = createHashRouter(
        createRoutesFromElements(
            <Route path="/" element={<ApplicationFrame><Outlet/></ApplicationFrame>}>
                {
                    !auth.userData ? (
                        <>
                            <Route path="/" element={<div/>}/>
                            <Route path="*" element={<Navigate to="/"/>}/>
                        </>
                    ) : (
                        <>
                            <Route path="/" element={<Navigate to="/lists"/>}/>
                            <Route path="/lists" element={<ShoppingListTableRoute/>}/>
                            <Route path="/lists/:listId" element={<ShoppingListItemRoute/>}/>
                            <Route path="*" element={<NoMatch/>}/>
                        </>
                    )
                }
            </Route>
        )
    )

    return <RouterProvider router={router}/>
}