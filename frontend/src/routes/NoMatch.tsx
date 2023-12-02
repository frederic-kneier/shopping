import {useLocation, useNavigate} from "react-router-dom";

export interface NoMatchProps {
    location?: string
    title?: string
}

export default function NoMatch(props: NoMatchProps) {
    const location = useLocation()
    const navigate = useNavigate()

    return (
        <div>
            <h3>
                No match for <code>{location.pathname}</code>.
            </h3>
            <p>
                Return to <a href="#" onClick={() => navigate(props.location ?? '/')}>{props.title ?? 'Home'}</a>
            </p>
        </div>
    )
}