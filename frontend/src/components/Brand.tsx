import { Link } from "react-router-dom";

export function Brand({ compact = false }: { compact?: boolean }) {
  return (
    <Link to="/" className="brand" aria-label="Core Global Bank home">
      <img src="/brand-mark.svg" alt="" />
      {!compact && <span><b>CORE</b><small>GLOBAL BANK</small></span>}
    </Link>
  );
}

