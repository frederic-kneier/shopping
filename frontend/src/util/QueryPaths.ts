export function listQueryKey(id?: string) {
    return ['lists', ...(id ? [id] : [])]
}

export function entryQueryKey(listId: string, id?: string) {
    return [...(listQueryKey(listId)), 'entries', ...(id ? [id] : [])]
}