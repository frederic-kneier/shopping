import {createContext, useContext} from "react";

export type Config = {
    issuerUri: string
    publicClientId: string,
}

const context = createContext<Config|undefined>(undefined)

export const ConfigProvider = context.Provider

export function useConfig() {
    const result = useContext(context)
    if (!result) throw new Error("Config is not available")
    return result
}