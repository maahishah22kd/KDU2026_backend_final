import { configureStore } from '@reduxjs/toolkit';
import registerReducer from '../features/registration/registrationSlice';
import statusReducer from '../features/registration/registrationSlice';

export const store = configureStore({
  reducer: {
   register: registerReducer,
   confirmation: statusReducer
  },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;