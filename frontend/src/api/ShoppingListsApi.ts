import useApi from "./Api.ts";
import ShoppingList from "../model/ShoppingList.ts";


export type ShoppingListCreate = Pick<ShoppingList, 'title'>

export default function useShoppingListApi() {
    const api = useApi()

    return {
        queryAll: async () => {
            const response = await api.get<ShoppingList[]>('/lists')
            return response.data
        },
        queryOne: (id: string) => async () => {
            const response = await api.get<ShoppingList>(`/lists/${id}`)
            return response.data
        },
        create: async (data: ShoppingListCreate) => {
            return api.post('/lists', data)
        },
        delete: async (id: string) => {
            return api.delete(`/lists/${id}`)
        },
    }
}