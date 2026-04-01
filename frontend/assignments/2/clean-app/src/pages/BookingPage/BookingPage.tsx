import styles from "./BookingPage.module.scss";
import fridgeImg from "../../assets/fridge.png";
import windowImg from "../../assets/window.png";
import ovenImg from "../../assets/oven.jpg";
import ironImg from "../../assets/iron.jpg";
import bedImg from "../../assets/bed.png";
import showerImg from "../../assets/shower.png";
import CounterCard from "../../components/Countercard/CounterCard";
import {useEffect} from "react";
import {useDispatch,useSelector} from "react-redux";
import {fetchConfigThunk,submitBookingThunk} from "../../features/booking/bookingsThunks";
import type {RootState,AppDispatch} from "../../app/store";
import {useNavigate} from "react-router-dom";
import{
  setCleaningType,
  setFrequency,
  setBedrooms,
  setBathrooms,
  setHours,
  setDate,
  setTimeSlot,
  toggleExtra,
  setRequirements,
  updateCardField,
  updatePersonalField,
  setTermsAccepted,
} from "../../features/booking/bookingSlice";

export default function BookingPage() {
  const EXTRA_ICONS: Record<string, string> = {
  oven: ovenImg,
  windows: windowImg,
  fridge: fridgeImg,
  ironing: ironImg,
};


  const dispatch = useDispatch<AppDispatch>();
  const navigate= useNavigate();

  const configStatus= useSelector((state: RootState)=>state.config.status);
  const config=useSelector((state:RootState)=>state.config.data);
  const form = useSelector((state: RootState)=> state.booking.form);
  const submitStatus= useSelector((state: RootState)=>state.booking.submitStatus);
  const isSubmitting=submitStatus==="loading";

  useEffect(() => {
    dispatch(fetchConfigThunk());
  }, [dispatch]);

  useEffect(() => {
  if (submitStatus==="succeeded") {
    navigate("/confirmation");
  }
}, [submitStatus,navigate]);

  if(configStatus==="loading") return <div>Loading config...</div>;
  if(configStatus==="failed") return <div>Config failed to load</div>;

const cleaningTypeObj= config?.cleaningTypes?.find((c)=>c.id ===form.cleaningType)??null;
const frequencyObj= config?.frequencies?.find((f)=>f.id ===form.frequency)??null;

const selectedExtras= (config?.extras?? []).filter((ex)=>form.extras.includes(ex.id));

const timeSlotObj= config?.timeSlots?.find((t)=> t.id=== form.timeSlot)?? null;

const basePrice= cleaningTypeObj?.basePrice??0;
const extrasTotal= selectedExtras.reduce((sum, ex) =>sum+(ex.price??0),0);
const totalCost= basePrice*(form.hours||1)+ extrasTotal;


  return (
    <div className={styles.book}>
      <div className={styles.book__title}>
        Book your cleaning
      </div>

      <div className={styles.book__subtitle}>
        Its time to book pur cleaning services for your apartment or home.
      </div>

      <div className={styles.book__wrapper}>
        <div className={styles.book__booking}>
          <div className={styles.book__bookingheader}>
            Cleaning Preferences
          </div>

          <div className={styles.book__form}>
            <div className={styles.book__formquestions}>
              What type of cleaning?
            </div>

            <div className={styles.book__btn}>
              {config?.cleaningTypes.map((ct)=>(
                <label key={ct.id} className={styles.book__option}>
                  <input
                    type="radio"
                    name="cleaningType"
                    required
                    value={ct.id}
                    checked={form.cleaningType===ct.id}
                    onChange={()=>dispatch(setCleaningType(ct.id))}
                  />
                  <span>{ct.label.toUpperCase()}</span>
                </label>
              ))}
            </div>

          </div>

          <div className={styles.book__form}>
            <div className={styles.book__formquestions}>
              How often would you like cleaning?
            </div>

            <div className={styles.book__btn}>
              {config?.frequencies.map((f) => (
                <label key={f.id} className={styles.book__option}>
                  <input
                    type="radio"
                    required
                    name="frequency"
                    value={f.id}
                    checked={form.frequency===f.id}
                    onChange={()=>dispatch(setFrequency(f.id))}
                  />
                  <span>{f.label.toUpperCase()}</span>
                </label>
              ))}
            </div>

          </div>

          <hr className={styles.book__line} />
          <p className={styles.book__about}>Tell us about your home</p>
          <hr className={styles.book__line} />

            <div className={styles.book__counters}>
                <CounterCard
                    icon={bedImg}
                    label="BEDROOMS"
                    value={form.bedrooms}
                    onChange={(val)=>dispatch(setBedrooms(val))}
                    min={0}
                    max={10}
                />

                <CounterCard
                    icon={showerImg}
                    label="BATHROOMS"
                    value={form.bathrooms}
                    onChange={(val)=>dispatch(setBathrooms(val))}
                    min={0}
                    max={10}
                />
            </div>

            
          <div className={styles.book__formquestions}>
            Need extras?
          </div>

          <div className={styles.book__btn}>
           {config?.extras.map((ex) => (
        <label key={ex.id} className={styles.book__option}>
          <input
            type="checkbox"
            checked={form.extras.includes(ex.id)}
            onChange={()=>dispatch(toggleExtra(ex.id))}
          />
          <span>
            {EXTRA_ICONS[ex.id] && <img src={EXTRA_ICONS[ex.id]} alt={ex.label} />}
            {ex.label.toUpperCase()}
          </span>
        </label>
      ))}

          </div>


          <div className={styles.book__formquestions}>
            Do you have any special requirements?
          </div>

          <textarea className={styles.book__req}
          value={form.requirements}
            onChange={(e)=>dispatch(setRequirements(e.target.value))}></textarea>

          <hr className={styles.book__line} />
          <p className={styles.book__about}>Choose hours and dates</p>
          <hr className={styles.book__line} />
            
            <div className={styles.book__timewrapper}>
                <div className={styles.book__hours}>
                    <div className={styles.book__formquestions}>How many hours?</div>
                <CounterCard
                    icon={""}
                    label=""
                    value={form.hours}
                  onChange={(val)=>dispatch(setHours(val))}
                    min={0}
                    max={10}
                />
                </div>

                <div className={styles.book__date}>
                    <div className={styles.book__formquestions}>Choose a date?</div>
                    <input type="date"
                    required
                    value={form.date}
                onChange={(e)=>dispatch(setDate(e.target.value))}></input>
                </div>
            </div>

          <div className={styles.book__datetime}>
            <div className={styles.book__formquestions}>
              When do you like to start?
            </div>

            <div className={styles.book__btn}>
              {config?.timeSlots.map((t) => (
                <label key={t.id} className={styles.book__option}>
                  <input
                    type="radio"
                    name="timeSlot"
                    value={t.id}
                    checked={form.timeSlot===t.id}
                    onChange={()=>dispatch(setTimeSlot(t.id))}
                    disabled={!t.available}
                  />
                  <span style={{ opacity:t.available?1:0.4}}>
                    {t.label}
                  </span>
                </label>
              ))}
            </div>

          </div>

          <div className={styles.book__pay}>
            <hr className={styles.book__line} />
            <p className={styles.book__about}>Payment Method</p>
            <hr className={styles.book__line} />

            <div className={styles.book__cardinfo}>
              <div className={styles.book__formquestions}>
                Credit Card details
              </div>

              <input type="number" placeholder="Card number" 
              value={form.cardNumber}
              required
                onChange={(e) =>
                  dispatch(updateCardField({field:"cardNumber",value:e.target.value }))
                }/>

              <div className={styles.book__pin}>
                <input
                  type="text"
                  placeholder="MM/YY"
                  required
                  value={form.expiration}
                  onChange={(e)=>
                    dispatch(updateCardField({field:"expiration",value:e.target.value }))
                  }
                />
                <input
                  type="text"
                  placeholder="CVV"
                  required
                  value={form.cvv}
                  onChange={(e)=>
                    dispatch(updateCardField({field:"cvv",value: e.target.value }))
                  }
                />
                <input
                  type="text"
                  placeholder="Name as on card"
                  required
                  value={form.name}
                  onChange={(e) =>
                    dispatch(updateCardField({field:"name",value: e.target.value }))
                  }
                />
              </div>
            </div>

            <div className={styles.book__cardinfo}>
              <div className={styles.book__formquestions}>
                Personal details
              </div>

              <div className={styles.book__email}>
                <input type="email" placeholder="Email Address" 
                required
                value={form.email}
                  onChange={(e) =>
                    dispatch(updatePersonalField({field:"email", value:e.target.value }))
                  }/>
                <input type="number" placeholder="Phone Number" 
                required
                value={form.phone}
                  onChange={(e) =>
                    dispatch(updatePersonalField({field: "phone",value:e.target.value }))
                  }/>
              </div>

              <div className={styles.book__address}>
                <input type="text" placeholder="Your Full Address" 
                required
                value={form.address}
                  onChange={(e) =>
                    dispatch(updatePersonalField({ field:"address",value: e.target.value }))
                  }/>
                <input type="number" placeholder="PinCode" 
                required
                value={form.pincode}
                  onChange={(e) =>
                    dispatch(updatePersonalField({field:"pincode", value:e.target.value }))
                  }/>
              </div>
            </div>

            <div className={styles.book__terms}>
              <label>
                <input type="checkbox" />
                Check this custom checkbox
              </label>
            <br></br>
              <label>
                <input type="checkbox"
                checked={form.termsAccepted}
                  onChange={(e) => dispatch(setTermsAccepted(e.target.checked))}/>
                I read and agree to the terms and conditions
              </label>
            </div>
          </div>


            <div className={styles.book__submit}>
                <button type="button"
                disabled={!form.termsAccepted||isSubmitting}
              onClick={()=>{
                  dispatch(submitBookingThunk(form))}}>
                    Complete Booking via Secure Server
                </button>
            </div>
        </div>

        <div className={styles.book__summary}>
  <div className={styles.book__bookingheader}>Booking Summary</div>

  <div className={styles.book__summaryBody}>
    <div className={styles.book__summaryRow}>
      <span>Cleaning</span>
      <span>{cleaningTypeObj?.label??"-"}</span>
    </div>

    <div className={styles.book__summaryRow}>
      <span>Frequency</span>
      <span>{frequencyObj?.label??"-"}</span>
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
      <span>
        {form.date || "-"}
        {timeSlotObj?.label ?` ${timeSlotObj.label}`: ""}
      </span>
    </div>

    <div className={styles.book__summarySectionTitle}>Extras</div>

    {selectedExtras.length===0?(
      <div className={styles.book__summaryMuted}>No extras selected</div>
    ):(
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


      </div>
    </div>
  );
}