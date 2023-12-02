import ShoppingEntry from "../model/ShoppingEntry.ts";
import {
    Checkbox,
    FormControlLabel,
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
import ClassNames from "./ShoppingEntryTable.module.css";
import AddIcon from "../icons/AddIcon.tsx";

export type ShoppingEntryTableProps = {
    items: ShoppingEntry[],
    onItemDelete?: (item: ShoppingEntry) => void,
    onItemChange?: (item: ShoppingEntry) => void,
    onItemAdd?: () => void,
}

export default function ShoppingEntryTable(props: ShoppingEntryTableProps) {
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
                        props.items.sort((a, b) => a.state > b.state ? -1 : 1).map(item => (
                            <TableRow key={item.id}>
                                <TableCell>
                                    <FormControlLabel
                                        label={
                                            <span><b>{item.amount}</b> {item.title}</span>
                                        }
                                        control={
                                            <Checkbox
                                                checked={item.state === 'DONE'}
                                                onChange={() => props.onItemChange?.(item)}
                                            />
                                        }
                                    />
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