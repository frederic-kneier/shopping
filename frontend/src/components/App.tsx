
import Classes from "./App.module.css"
import {AppBar, IconButton, Toolbar, Typography} from "@mui/material";
import MenuIcon from "../icons/MenuIcon.tsx";
export default function App() {
    return (
        <div className={Classes.frame}>
            <header className={Classes.header}>
                <AppBar position="relative">
                    <Toolbar>
                        <IconButton edge="start">
                            <MenuIcon />
                        </IconButton>
                        <Typography variant="h6">
                            Tasks
                        </Typography>
                    </Toolbar>
                </AppBar>
            </header>
            <main className={Classes.content}>Content</main>
        </div>
    )
}

