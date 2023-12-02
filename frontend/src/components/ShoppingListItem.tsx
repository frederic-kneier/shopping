import ShoppingList from "../model/ShoppingList.ts";

export type ShoppingListEntryProps = {
    item: ShoppingList
}

export default function ShoppingListItem(props: ShoppingListEntryProps) {
    return <div>{props.item.title}</div>
}