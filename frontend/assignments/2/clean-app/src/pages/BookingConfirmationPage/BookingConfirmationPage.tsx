import {useSelector} from "react-redux";
import type {RootState} from "../../app/store";
import styles from "./BookingConfirmationPage.module.scss";

export default function BookingConfirmationPage(){
    const submitStatus=useSelector((state:RootState)=>state.booking.submitStatus);
    const submitError=useSelector((state:RootState)=>state.booking.submitError);
    const confirmation=useSelector((state:RootState)=>state.booking.confirmation);
    const form = useSelector((state: RootState) => state.booking.form);
    const config=useSelector((state:RootState)=>state.config.data);

    if(submitStatus==="loading") return <div>Processing your booking...</div>
    if(submitStatus==="failed") return <div>{submitError??"Booking failed"}</div>


const cleaningTypeObj= config?.cleaningTypes?.find((c)=>c.id ===form.cleaningType)??null;
const frequencyObj= config?.frequencies?.find((f)=>f.id ===form.frequency)??null;

const selectedExtras= (config?.extras??[]).filter((ex)=>form.extras.includes(ex.id));

const timeSlotObj= config?.timeSlots?.find((t)=> t.id=== form.timeSlot)?? null;

const basePrice= cleaningTypeObj?.basePrice??0;
const extrasTotal= selectedExtras.reduce((sum, ex) =>sum+(ex.price??0),0);
const totalCost= basePrice*(form.hours||1)+ extrasTotal;

const startText = form.date
    ?`${form.date}${timeSlotObj?.label?`@ ${timeSlotObj.label}`:""}`
    :"-";
return(
     <div className={styles.confirm__mssg}>
        <h2>BOOKING CONFIRMED</h2>

        <div className={styles.book__summaryBody}>
        <div className={styles.book__summaryRow}>
          <span>Booking ID</span>
          <span>{confirmation?.bookingId ?? "-"}</span>
        </div>

        <div className={styles.book__summaryRow}>
          <span>Cleaning</span>
          <span>{cleaningTypeObj?.label ?? "-"}</span>
        </div>

        <div className={styles.book__summaryRow}>
          <span>Frequency</span>
          <span>{frequencyObj?.label ?? "-"}</span>
        </div>

        <div className={styles.book__summaryRow}>
          <span>Bedrooms</span>
          <span>{form.bedrooms}</span>
        </div>

        <div className={styles.book__summaryRow}>
          <span>Bathrooms</span>
          <span>{form.bathrooms}</span>
        </div>

        <div className={styles.book__summaryRow}>
          <span>Hours</span>
          <span>{form.hours}</span>
        </div>

        <div className={styles.book__summaryRow}>
          <span>Start</span>
          <span>{startText}</span>
        </div>

        <div className={styles.book__summarySectionTitle}>Extras</div>

        {selectedExtras.length === 0 ? (
          <div className={styles.book__summaryMuted}>No extras selected</div>
        ) : (
          selectedExtras.map((ex) => (
            <div key={ex.id} className={styles.book__summaryRow}>
              <span>{ex.label}</span>
              <span>Rs.{ex.price}</span>
            </div>
          ))
        )}

        <div className={styles.book__summaryDivider} />

        <div className={styles.book__summaryTotal}>
          <span>Total cost</span>
          <span>Rs.{totalCost}</span>
        </div>
      </div>
    </div>
  );
}
