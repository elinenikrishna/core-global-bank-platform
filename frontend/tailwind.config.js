/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"],
  theme: {
    extend: {
      fontFamily: {
        sans: ["Manrope", "system-ui", "sans-serif"],
        display: ["Cormorant Garamond", "Georgia", "serif"]
      },
      colors: {
        ink: "#07110f",
        night: "#0a1714",
        gold: "#c8a55a",
        ivory: "#f3efe3",
        mint: "#7ce2bd"
      }
    }
  },
  plugins: []
};

