import {useMemo} from "react";
import {useAuth} from "oidc-react";
import axios from "axios";

export default function useApi() {
    const auth = useAuth()
    return useMemo(() => {
        const result = axios.create({
            baseURL: '/api',
        })
        result.interceptors.request.use(request => {
            if (auth.userData) {
                request.headers.setAuthorization(`Bearer ${auth.userData.access_token}`)
            }
            return request
        })
        result.interceptors.response.use( undefined, error => {
            if (error.response.status === 401) {
                auth.signOut().then()
            }
            return error
        })
        return result
    }, [auth])
}