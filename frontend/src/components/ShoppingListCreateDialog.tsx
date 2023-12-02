import {Button, Dialog, DialogActions, DialogContent, DialogProps, DialogTitle, TextField} from "@mui/material";
import {ChangeEvent, useState} from "react";
import {ShoppingListCreate} from "../api/ShoppingListsApi.ts";


export interface ShoppingListEditDialogProps extends DialogProps {
    onResult: (params: ShoppingListCreate | null) => void
}

export default function ShoppingListCreateDialog(props: ShoppingListEditDialogProps) {
    const [form, setForm] = useState<ShoppingListCreate>({title: ''})

    function onChange(e: ChangeEvent<HTMLInputElement>) {
        setForm((c) => ({...c, [e.target.name]: e.target.value}))
    }

    function onSubmit() {
        props.onResult(form)
        setForm({title: ''})
    }

    return (
        <Dialog {...props}>
            <DialogTitle>Create a new shopping list</DialogTitle>
            <DialogContent>
                <TextField
                    name="title"
                    label="Title"
                    variant="standard"
                    fullWidth
                    value={form.title}
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