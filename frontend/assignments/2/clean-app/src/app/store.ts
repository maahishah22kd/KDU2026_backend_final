import { configureStore } from '@reduxjs/toolkit';
import configReducer from '../features/config/configSlice';
import bookingReducer from '../features/booking/bookingSlice';


export const store = configureStore({
  reducer: {
    config: configReducer,
    booking: bookingReducer,
  },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;