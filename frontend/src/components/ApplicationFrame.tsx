import {useAuth} from "oidc-react";
import {useNavigate} from "react-router-dom";
import Classes from "./App.module.css";
import {AppBar, IconButton, Toolbar, Tooltip, Typography} from "@mui/material";
import ListsIcon from "../icons/ListsIcon.tsx";
import LogoutIcon from "../icons/LogoutIcon.tsx";
import LoginIcon from "../icons/LoginIcon.tsx";
import {PropsWithChildren} from "react";

export default function ApplicationFrame(props: PropsWithChildren) {
    const auth = useAuth()
    const userData = auth.userData
    const profile = userData?.profile
    const navigate = useNavigate()

    return (
        <div className={Classes.frame}>
            <AppBar className={Classes.header} position="relative">
                <Toolbar>
                    <IconButton color="inherit" edge="start" onClick={() => navigate('/lists')}>
                        <ListsIcon/>
                    </IconButton>
                    <Typography variant="h6" sx={{flexGrow: 1}}>
                        Tasks
                    </Typography>
                    {auth.userData ? (
                        <Tooltip color="inherit" title={`Logout (${profile?.given_name ?? profile?.sub})`}>
                            <IconButton edge="end" onClick={() => auth.signOut()}>
                                <LogoutIcon/>
                            </IconButton>
                        </Tooltip>
                    ) : (
                        <Tooltip title="Login">
                            <IconButton color="inherit" edge="end" onClick={() => auth.signIn()}>
                                <LoginIcon/>
                            </IconButton>
                        </Tooltip>
                    )}
                </Toolbar>
            </AppBar>
            <main className={Classes.content}>
                {props.children}
            </main>
        </div>
    )
}