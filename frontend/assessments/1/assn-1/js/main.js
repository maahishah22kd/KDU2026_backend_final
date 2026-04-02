document.addEventListener('DOMContentLoaded', () => {
  const qtyInput = document.querySelector('.qty');
  const historyList = document.querySelector('.right-content .history');
  const priceEl = document.querySelector('.control-center .price-value');
  const ARROW_UP = '\u25B2'; 
  const ARROW_DOWN = '\u25BC'; 

function rand(min, max) {
  return Math.floor(Math.random()*(max-min+1)) + min;
}
const fmtDate= d=>d.toUTCString();

const createCard =({qty, action}) => {
    const el = document.createElement('div');
    el.className = 'history-card';
    el.innerHTML = `
      <div class="history-top">
        <strong>${qty} stocks</strong>
        <span class="action ${action.toLowerCase()}">${action}</span>
      </div>
      <div class="history-time">${fmtDate(new Date())}</div>
    `;
    return el;
};

  const barsEl= document.querySelector('.bars');
  function addBar(price, action, qty = 0){
    if (!barsEl) return;
    const maxScale = 500;
    const heightPct = Math.max(6, (price / maxScale) * 100);
    const bar = document.createElement('div');
    const cls = action && action.toLowerCase() === 'buy' ?'green': 'red';
    bar.className = `bar ${cls}`;
    bar.style.width = '20px';
    bar.style.height = `${heightPct}%`;
    bar.title = `${action} ${qty ? '• ' + qty + ' shares ' : ''}@ ${price}`;
    barsEl.appendChild(bar);
    barsEl.scrollLeft = barsEl.scrollWidth;
  }

  document.addEventListener('click',(e) => {
    const btn = e.target.closest('.btn-buy, .btn-sell');
    if (!btn) return;
    const action = btn.classList.contains('btn-buy') ?'Buy':'Sell';
    const qty = parseInt(qtyInput.value, 10);
    if (!qty||qty <= 0) return qtyInput.focus();
    historyList.appendChild(createCard({ qty, action }));
    addBar(priceNum, action, qty);
    qtyInput.value = '';
  });


  let lastPrice = priceEl?parseFloat(priceEl.textContent)||rand(1,300):rand(1,300);
  if (priceEl) priceEl.innerHTML= `${lastPrice.toFixed(2)} <span class="change positive">${ARROW_UP} 0.00%</span>`; 

  setInterval(() => {
    const newPrice = parseFloat((rand(1, 300)+Math.random()).toFixed(2));
    const diff= newPrice- lastPrice;
    const cls= diff >= 0 ? 'positive':'negative';
    const arrow =diff>= 0 ? ARROW_UP :ARROW_DOWN;
    const pct= lastPrice ? Math.abs((diff/lastPrice)*100).toFixed(2):'0.00';
    if (priceEl) priceEl.innerHTML= `${newPrice.toFixed(2)} <span class="change ${cls}">${arrow} ${pct}%</span>`;
    addBar(newPrice, diff >= 0 ?'Buy':'Sell');
    lastPrice = newPrice;
  }, 5000);
});

