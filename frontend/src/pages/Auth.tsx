import { ArrowRight, Check, ShieldCheck } from "lucide-react";
import { useNavigate } from "react-router-dom";
import { Brand } from "../components/Brand";

export function AuthPage({ register = false }: { register?: boolean }) {
  const navigate = useNavigate();

  return (
    <div className="auth-page">
      <div className="auth-art">
        <Brand />

        <div className="auth-art-copy">
          <span className="section-kicker light">Portfolio demonstration</span>
          <h1>
            Core Global Bank
            <br />
            <em>Fictional fintech platform.</em>
          </h1>
          <p>
            This is a software engineering portfolio project. It is not a real
            financial institution and does not collect personal, banking, or
            financial information.
          </p>

          <ul>
            <li><Check />Mock financial dashboard</li>
            <li><Check />Simulated fraud monitoring</li>
            <li><Check />Demo data only</li>
          </ul>
        </div>

        <div className="auth-orbit ao-one" />
        <div className="auth-orbit ao-two" />
        <div className="auth-shield"><ShieldCheck size={38} /></div>
      </div>

      <div className="auth-form-wrap">
        <div className="auth-mobile-brand"><Brand /></div>

        <div className="auth-demo-panel">
          <div className="auth-title">
            <span>Portfolio Demo Access</span>
            <h2>Explore the platform</h2>
            <p>
              No login is required. This demo uses fictional sample data only.
            </p>
          </div>

          <div className="auth-security">
            <ShieldCheck size={17} />
            Educational portfolio project only
          </div>

          <button
            className="button button-dark auth-submit"
            onClick={() => navigate("/dashboard")}
          >
            Explore Demo Dashboard <ArrowRight size={17} />
          </button>

          <p className="auth-switch">
            Core Global Bank is fictional and not connected to any real bank.
          </p>
        </div>
      </div>
    </div>
  );
}