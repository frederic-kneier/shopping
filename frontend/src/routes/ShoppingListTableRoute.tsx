import useShoppingListApi, {ShoppingListCreate} from "../api/ShoppingListsApi.ts";
import {useNavigate} from "react-router-dom";
import ShoppingListTable from "../components/ShoppingListTable.tsx";
import ShoppingListCreateDialog from "../components/ShoppingListCreateDialog.tsx";
import {useState} from "react";
import {useMutation, useQuery, useQueryClient} from "react-query";
import ShoppingList from "../model/ShoppingList.ts";
import {listQueryKey} from "../util/QueryPaths.ts";

export default function ShoppingListTableRoute() {
    const [edit, setEdit] = useState(false)

    const api = useShoppingListApi()
    const client = useQueryClient()
    const navigate = useNavigate()
    const allQuery = useQuery(listQueryKey(), api.queryAll)
    const createMutation = useMutation(api.create, {
        onSuccess: () => client.invalidateQueries(listQueryKey())
    })
    const deleteMutation = useMutation(api.delete, {
        onSuccess: () => client.invalidateQueries(listQueryKey())
    })

    function navigateList(item: ShoppingList) {
        navigate(`/lists/${item.id}`)
    }

    function createItem(params: ShoppingListCreate | null) {
        if (params) {
            createMutation.mutate(params)
        }
        setEdit(false)
    }

    function deleteItem(list: ShoppingList) {
        deleteMutation.mutate(list.id)
    }

    return (
        <>
            <ShoppingListTable
                items={allQuery.data ?? []}
                onItemClick={navigateList}
                onItemDelete={deleteItem}
                onItemAdd={() => setEdit(true)}
            />
            <ShoppingListCreateDialog
                onResult={createItem}
                open={edit}
            />
        </>
    )
}