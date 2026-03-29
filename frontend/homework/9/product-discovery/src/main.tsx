import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import App from './App.tsx'
import { BrowserRouter } from "react-router-dom";
import "./styles/styles.scss";
import { Provider } from "react-redux";
import {store} from "./store/store";

createRoot(document.getElementById('root')!).render(
  //could have usde lazy loading call for pages- lazy(): module-default
  //error boundary  not used
  //Suspense not used


  <StrictMode>
    <Provider store={store}>
      <BrowserRouter>
        <App />
      </BrowserRouter>
    </Provider>
  </StrictMode>, // It intentionally runs some things “more than once” in development to expose bugs that would otherwise be hidden.
)


// loadash
// const lodash = require('lodash');
// Using lodash.debounce() method
// with its parameters
// let debounce_fun = lodash.debounce(function () {
//     console.log('Function debounced after 1000ms!');
// }, 1000);
// debounce_fun();