export default interface ShoppingEntry {
    id: string
    title: string
    amount: number
    state: 'PENDING' | 'DONE'
}