import { useState } from "react";
import { MessageCircle, Send, X, Sparkles } from "lucide-react";
import { AnimatePresence, motion } from "framer-motion";

export function ChatWidget() {
  const [open, setOpen] = useState(false);
  const [messages, setMessages] = useState(["Hello, I’m Cora. How can I help with your banking today?"]);
  const [text, setText] = useState("");
  const send = () => {
    if (!text.trim()) return;
    setMessages([...messages, text, "I can help with that. I’ve surfaced the most relevant secure options for you."]);
    setText("");
  };
  return (
    <>
      <AnimatePresence>
        {open && <motion.div className="chat-panel" initial={{ opacity: 0, y: 25, scale: .95 }} animate={{ opacity: 1, y: 0, scale: 1 }} exit={{ opacity: 0, y: 20, scale: .95 }}>
          <div className="chat-head"><span><Sparkles size={18} /> Cora · Digital concierge</span><button onClick={() => setOpen(false)}><X size={18} /></button></div>
          <div className="chat-body">
            {messages.map((m, i) => <div key={i} className={i % 2 ? "chat-user" : "chat-bot"}>{m}</div>)}
            <div className="chat-suggestions"><button onClick={() => setMessages([...messages, "Show recent activity", "Your latest activity is ready in Transactions."])}>Recent activity</button><button onClick={() => setMessages([...messages, "I need card help", "Your card is active. You can freeze it instantly from Card controls."])}>Card help</button></div>
          </div>
          <div className="chat-input"><input value={text} onChange={e => setText(e.target.value)} onKeyDown={e => e.key === "Enter" && send()} placeholder="Ask Cora…" /><button onClick={send}><Send size={17} /></button></div>
        </motion.div>}
      </AnimatePresence>
      <button className="chat-fab" onClick={() => setOpen(!open)} aria-label="Open support chat"><MessageCircle size={22} /><span>Ask Cora</span></button>
    </>
  );
}
