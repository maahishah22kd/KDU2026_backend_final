const API_BASE_URL = ""; // example: https://abc123.execute-api.ap-south-1.amazonaws.com/prod

document.getElementById("api-url").innerText = API_BASE_URL || "not set yet";

async function getCounter() {
  if (!API_BASE_URL) return;

  const res = await fetch(`${API_BASE_URL}/counter`);
  const data = await res.json();
  document.getElementById("counter").innerText = data.value;
}

async function increment() {
  if (!API_BASE_URL) {
    alert("API not wired yet. We'll connect it later.");
    return;
  }

  const res = await fetch(`${API_BASE_URL}/counter`, { method: "PUT" });
  const data = await res.json();
  document.getElementById("counter").innerText = data.value;
}

getCounter();