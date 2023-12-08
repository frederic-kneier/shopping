import React from 'react'
import ReactDOM from 'react-dom/client'
import {createTheme, CssBaseline, ThemeProvider} from "@mui/material";
import {QueryClient, QueryClientProvider} from "react-query";
import axios from "axios";
import {Config, ConfigProvider} from "./contexts/Config.ts";
import {AuthProvider, AuthProviderProps} from "oidc-react";
import Routing from "./routes/Routing.tsx";

const theme = createTheme({})
const queryClient = new QueryClient()

axios.get<Config>("/api/config").then(response => {
    const config = response.data
    const authConfig: Partial<AuthProviderProps> = {
        authority: config.issuerUri,
        extraQueryParams: {
            audience: window.location.origin,
        },
        loadUserInfo: false,
        silentRedirectUri: window.location.origin,
        scope: 'openid profile email',
        clientId: config.publicClientId,
        redirectUri: window.location.origin,
        onSignIn: () => {
            history.pushState({}, '', location.href.split('?')[0]);
        },
    }
    ReactDOM.createRoot(document.getElementById('root')!).render(
        <React.StrictMode>
            <CssBaseline>
                <ThemeProvider theme={theme}>
                    <ConfigProvider value={config}>
                        <QueryClientProvider client={queryClient}>
                            <AuthProvider {...authConfig}>
                                <Routing/>
                            </AuthProvider>
                        </QueryClientProvider>
                    </ConfigProvider>
                </ThemeProvider>
            </CssBaseline>
        </React.StrictMode>
    )
})

