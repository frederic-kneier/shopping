import useApi from "./Api.ts";
import ShoppingEntry from "../model/ShoppingEntry.ts";


export type ShoppingEntryCreate = Pick<ShoppingEntry, 'title' | 'amount'>
export type ShoppingEntryUpdate = Pick<ShoppingEntry, 'title' | 'amount' | 'state'>

export default function useShoppingEntryApi() {
    const api = useApi()

    return {
        queryAll: (listId: string) => async () => {
            const response = await api.get<ShoppingEntry[]>(`/lists/${listId}/entries`)
            return response.data
        },
        queryOne: (listId: string, id: string) => async () => {
            const response = await api.get<ShoppingEntry>(`/lists/${listId}/entries/${id}`)
            return response.data
        },
        create: (listId: string) => async (variables: { payload: ShoppingEntryCreate }) => {
            return api.post(`/lists/${listId}/entries`, variables.payload)
        },
        update: (listId: string) => async (variables: { id: string, payload: ShoppingEntry }) => {
            return api.post(`/lists/${listId}/entries/${variables.id}`, variables.payload)
        },
        delete: (listId: string) => async (variables: { id: string }) => {
            return api.delete(`/lists/${listId}/entries/${variables.id}`)
        },
    }
}