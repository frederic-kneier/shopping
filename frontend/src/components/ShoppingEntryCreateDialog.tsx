import {Button, Dialog, DialogActions, DialogContent, DialogProps, DialogTitle, TextField} from "@mui/material";
import {ChangeEvent, useState} from "react";
import {ShoppingEntryCreate} from "../api/ShoppingEntryApi.ts";

export interface ShoppingEntryEditDialogProps extends DialogProps {
    onResult: (params: ShoppingEntryCreate | null) => void
}

const formEmpty: ShoppingEntryCreate = {
    title: '',
    amount: 1,
}

export default function ShoppingEntryCreateDialog(props: ShoppingEntryEditDialogProps) {
    const [form, setForm] = useState<ShoppingEntryCreate>(formEmpty)

    function onChange(e: ChangeEvent<HTMLInputElement>) {
        setForm((c) => ({...c, [e.target.name]: e.target.value}))
    }

    function onSubmit() {
        props.onResult(form)
        setForm(formEmpty)
    }

    return (
        <Dialog {...props}>
            <DialogTitle>Create a new shopping list entry</DialogTitle>
            <DialogContent>
                <TextField
                    name="title"
                    label="Title"
                    type="text"
                    variant="standard"
                    fullWidth
                    value={form.title}
                    onChange={onChange}
                />
                <TextField
                    name="amount"
                    label="Amount"
                    type="number"
                    variant="standard"
                    fullWidth
                    value={form.amount}
                    onChange={onChange}
                />
            </DialogContent>
            <DialogActions>
                <Button onClick={() => props.onResult(null)}>Cancel</Button>
                <Button variant="contained" onClick={onSubmit}>Create</Button>
            </DialogActions>
        </Dialog>
    )
}