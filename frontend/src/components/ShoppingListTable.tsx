import ShoppingList from "../model/ShoppingList.ts";
import {
    Button,
    IconButton,
    Paper,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow
} from "@mui/material";
import DeleteIcon from "../icons/DeleteIcon.tsx";
import ClassNames from "./ShoppingListTable.module.css";
import AddIcon from "../icons/AddIcon.tsx";

export type ShoppingListTableProps = {
    items: ShoppingList[],
    onItemClick?: (item: ShoppingList) => void,
    onItemDelete?: (item: ShoppingList) => void,
    onItemAdd?: () => void,
}

export default function ShoppingListTable(props: ShoppingListTableProps) {
    return (
        <TableContainer component={Paper}>
            <Table className={ClassNames.table}>
                <TableHead>
                    <TableRow>
                        <TableCell>Name</TableCell>
                        <TableCell align="right">
                            {!props.onItemAdd ? null : (
                                <IconButton size="small" onClick={() => props.onItemAdd?.()}>
                                    <AddIcon/>
                                </IconButton>
                            )}
                        </TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {
                        props.items.map(item => (
                            <TableRow key={item.id}>
                                <TableCell>
                                    <Button variant="text" onClick={() => props.onItemClick?.(item)}>
                                        {item.title}
                                    </Button>
                                </TableCell>
                                <TableCell align="right">
                                    {!props.onItemDelete ? null : (
                                        <IconButton size="small" onClick={() => props.onItemDelete?.(item)}>
                                            <DeleteIcon/>
                                        </IconButton>
                                    )}
                                </TableCell>
                            </TableRow>
                        ))
                    }
                </TableBody>
            </Table>
        </TableContainer>
    )
}