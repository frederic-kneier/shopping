import {useParams} from "react-router-dom";
import {useMutation, useQuery, useQueryClient} from "react-query";
import useShoppingEntryApi, {ShoppingEntryCreate} from "../api/ShoppingEntryApi.ts";
import ShoppingEntryTable from "../components/ShoppingEntryTable.tsx";
import {useState} from "react";
import ShoppingEntry from "../model/ShoppingEntry.ts";
import ShoppingEntryCreateDialog from "../components/ShoppingEntryCreateDialog.tsx";
import {entryQueryKey} from "../util/QueryPaths.ts";

export default function ShoppingListItemRoute() {
    const [edit, setEdit] = useState(false)
    const {listId} = useParams()
    const api = useShoppingEntryApi()
    const client = useQueryClient()
    const entryQuery = useQuery(entryQueryKey(listId!), api.queryAll(listId!))
    const createMutation = useMutation(api.create(listId!), {
        onSuccess: () => client.invalidateQueries(entryQueryKey(listId!))
    })
    const deleteMutation = useMutation(api.delete(listId!), {
        onSuccess: () => client.invalidateQueries(entryQueryKey(listId!))
    })
    const updateMutation = useMutation(api.update(listId!), {
        onSuccess: () => client.invalidateQueries(entryQueryKey(listId!))
    })

    function createItem(payload: ShoppingEntryCreate | null) {
        if (payload) {
            createMutation.mutate({payload})
        }
        setEdit(false)
    }

    function switchItem(item: ShoppingEntry) {
        console.log({item})

        switch (item.state) {
            case 'DONE':
                item.state = 'PENDING'
                break

            default:
                item.state = 'DONE'
                break
        }

        return updateMutation.mutate({id: item.id, payload: item})
    }

    function deleteItem(item: ShoppingEntry) {
        deleteMutation.mutate({id: item.id})
    }

    return (
        <>
            <ShoppingEntryTable
                items={entryQuery.data ?? []}
                onItemAdd={() => setEdit(true)}
                onItemChange={switchItem}
                onItemDelete={deleteItem}
            />
            <ShoppingEntryCreateDialog
                open={edit}
                onResult={createItem}
            />
        </>
    )
}