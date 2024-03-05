package com.cinntra.indo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.cinntra.indo.R;
import com.cinntra.indo.activities.AddOrderAct;
import com.cinntra.indo.adapters.BranchTypeAdapter;
import com.cinntra.indo.adapters.CountryAdapter;
import com.cinntra.indo.adapters.StateAdapter;
import com.cinntra.indo.globals.Globals;
import com.cinntra.indo.interfaces.SubmitQuotation;
import com.cinntra.indo.model.AddressExtension;
import com.cinntra.indo.model.BPAddress;
import com.cinntra.indo.model.QuotationResponse;
import com.cinntra.indo.model.StateData;
import com.cinntra.indo.model.StateRespose;
import com.cinntra.indo.webservices.NewApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddOrderForm_Fianl_Fragment extends Fragment {
    @BindView(R.id.back_press)
    RelativeLayout back_press;
    @BindView(R.id.head_title)
    TextView head_title;
    @BindView(R.id.billing_name_value)
    EditText billing_name_value;
    @BindView(R.id.zip_code_value)
    EditText zip_code_value;

    @BindView(R.id.billing_address_value)
    EditText billing_address_value;
    @BindView(R.id.checkbox1)
    CheckBox checkbox1;
    @BindView(R.id.shipping_name_value)
    EditText shipping_name_value;
    @BindView(R.id.shipping_block)
    LinearLayout shipping_block;
    @BindView(R.id.done_button)
    Button done_button;
    @BindView(R.id.checkboxManager)
    RelativeLayout checkboxManager;
    @BindView(R.id.loader)
    ProgressBar loader;
    @BindView(R.id.ship_block)
    LinearLayout ship_block;

    @BindView(R.id.zipcode_value2)
    EditText zipcode_value2;
    @BindView(R.id.shipping_spinner2)
    Spinner shipping_spinner2;
    @BindView(R.id.shipping_spinner)
    Spinner shipping_spinner;
    @BindView(R.id.shipping_address_value)
    EditText shipping_address_value;
    @BindView(R.id.country_value)
    Spinner country_value;
    @BindView(R.id.state_value)
    Spinner state_value;
    @BindView(R.id.ship_country_value)
    Spinner ship_country_value;
    @BindView(R.id.ship_state_value)
    Spinner ship_state_value;
    @BindView(R.id.billing_branch_value)
    Spinner billing_branch_value;
    @BindView(R.id.shiping_branch_value)
    Spinner shiping_branch_value;
    @BindView(R.id.shipping_district_value)
     EditText shipping_district_value;
    @BindView(R.id.billing_district_value)
     EditText billing_district_value;

    SubmitQuotation submitQuotation;
    String [] shippinngType;
    String billshipType;
    String ship_shiptype;
    String billtoState,billtoStateCode,billtoCountrycode,billtoCountryName,shiptoState,shiptoCountrycode,shiptoCountryName,shiptoStateCode;
    CountryAdapter countryAdapter;
    StateAdapter stateAdapter,shipStateAdapter;
    ArrayList<StateData> stateList = new ArrayList<>();
    ArrayList<StateData> shipstateList = new ArrayList<>();

    public AddOrderForm_Fianl_Fragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static AddOrderForm_Fianl_Fragment newInstance(String param1, String param2) {
        AddOrderForm_Fianl_Fragment fragment = new AddOrderForm_Fianl_Fragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_add_qt_final, container, false);
        ButterKnife.bind(this,v);
        checkboxManager.setVisibility(View.VISIBLE);
        setDefaults();
        eventManageer();
        shippinngType  = getResources().getStringArray(R.array.shippingType);
        billshipType = shippinngType[0];
        ship_shiptype = shippinngType[0];
         submitQuotation = (SubmitQuotation)getActivity();

        done_button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (validation(billing_name_value.getText().toString().trim(),
                    billing_address_value.getText().toString().trim()))
              {
                AddressExtension addressExtension = new AddressExtension();
                addressExtension.setShipToDistrict(shipping_district_value.getText().toString().trim());
                addressExtension.setBillToDistrict(billing_district_value.getText().toString().trim());
                if(checkbox1.isChecked()){
                    addressExtension.setShipToZipCode(zipcode_value2.getText().toString().trim());
                    addressExtension.setShipToBuilding(shipping_name_value.getText().toString().trim());
                    addressExtension.setShipToStreet(shipping_address_value.getText().toString().trim());
                    addressExtension.setU_SSTATE(shiptoState);
                    addressExtension.setU_SCOUNTRY(shiptoCountryName);
                    addressExtension.setU_SHPTYPS(ship_shiptype);
                    addressExtension.setShipToState(shiptoStateCode);
                    addressExtension.setShipToCountry(shiptoCountrycode);

                }else{
                    addressExtension.setShipToZipCode(zip_code_value.getText().toString().trim());
                    addressExtension.setShipToBuilding(billing_name_value.getText().toString().trim());
                    addressExtension.setShipToStreet(billing_address_value.getText().toString().trim());
                    addressExtension.setU_SSTATE(billtoState);
                    addressExtension.setU_SCOUNTRY(billtoCountryName);
                    addressExtension.setU_SHPTYPS(billshipType);
                    addressExtension.setShipToState(billtoStateCode);
                    addressExtension.setShipToCountry(billtoCountrycode);

                }


                addressExtension.setShipToCity("");
               // addressExtension.setShipToZipCode(zipcode_value2.getText().toString());
                 //countryCode2
                addressExtension.setU_OPPRNM("");
                addressExtension.setU_BSTATE(billtoState);
                addressExtension.setU_BCOUNTRY(billtoCountryName);
                addressExtension.setBillToBuilding(billing_name_value.getText().toString());
                addressExtension.setBillToStreet(billing_address_value.getText().toString());
                addressExtension.setBillToCity("");
                addressExtension.setBillToZipCode(zip_code_value.getText().toString());
                addressExtension.setBillToState(billtoStateCode);
                addressExtension.setBillToCountry(billtoCountrycode);   //countryCode
                addressExtension.setU_SHPTYPB(billshipType);
                AddOrderAct.addQuotationObj.setAddressExtension(addressExtension);
                if (Globals.checkInternet(getActivity()))
                    submitQuotation.submitQuotaion(loader);
            }
        }
        });

        listFilter(AddOrderAct.addressData);

        return v;
    }

    private void eventManageer() {
        countryAdapter = new CountryAdapter(getContext(),Dashboard.countrylist);
        country_value.setAdapter(countryAdapter);
        country_value.setSelection(Globals.getCountrypos(Dashboard.countrylist,"India"));

        billtoCountrycode ="IN";
        billtoCountryName = "India";
        shiptoCountrycode = "IN";
        shiptoCountryName ="India";
        country_value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                billtoCountrycode = Dashboard.countrylist.get(position).getCode();
                billtoCountryName = Dashboard.countrylist.get(position).getName();
                billtoState = "";
                billtoStateCode = "";
                callStateApi(billtoCountrycode,"billto");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                billtoCountrycode = Dashboard.countrylist.get(0).getCode();
                billtoCountryName = Dashboard.countrylist.get(0).getName();
            }
        });

        state_value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                billtoState= stateList.get(position).getName();
                billtoStateCode= stateList.get(position).getCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if(stateList.size()>0){
                    billtoState= stateList.get(0).getName();
                    billtoStateCode= stateList.get(0).getCode();
                }

            }
        });

        ship_country_value.setAdapter(countryAdapter);
        ship_country_value.setSelection(Globals.getCountrypos(Dashboard.countrylist,"India"));

        ship_country_value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                shiptoCountrycode = Dashboard.countrylist.get(position).getCode();
                shiptoCountryName = Dashboard.countrylist.get(position).getName();
                shiptoState = "";
                shiptoStateCode = "";
                callStateApi(shiptoCountrycode,"shipto");
                }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                shiptoCountrycode = Dashboard.countrylist.get(0).getCode();
                shiptoCountryName = Dashboard.countrylist.get(0).getName();
            }
        });

        ship_state_value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(stateList.size()>0) {
                    shiptoState = stateList.get(position).getName();
                    shiptoStateCode = stateList.get(position).getCode();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                shiptoState= stateList.get(0).getName();
                shiptoStateCode= stateList.get(0).getCode();
            }
        });


      //  checkbox1.setChecked(true);
       // addressCheck(checkbox1.isChecked());
        checkbox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                addressCheck(isChecked);
            }
        });



        shipping_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
           {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                billshipType = shippinngType[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                billshipType = shippinngType[0];

            }
        });
        shipping_spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
           {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ship_shiptype = shippinngType[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ship_shiptype = shippinngType[0];
            }
        });
    }

    private void callStateApi(String countryCode, String code)
     {

        StateData stateData = new StateData();
        stateData.setCountry(countryCode);
        Call<StateRespose> call = NewApiClient.getInstance().getApiService().getStateList(stateData);
        call.enqueue(new Callback<StateRespose>() {
            @Override
            public void onResponse(Call<StateRespose> call, Response<StateRespose> response) {

                if(response.code()==200)
                {
                    if(code.equalsIgnoreCase("billto")){
                        stateList.clear();
                        if(response.body().getData().size()>0) {

                            stateList.addAll(response.body().getData());
                        }else{
                            StateData sta = new StateData();
                            sta.setName("Select State");
                            stateList.add(sta);
                        }
                        stateAdapter = new StateAdapter(getContext(),stateList);
                        state_value.setAdapter(stateAdapter);
                        stateAdapter.notifyDataSetChanged();
                        billtoState= stateList.get(0).getName();
                        billtoStateCode= stateList.get(0).getCode();
                    }else{
                        shipstateList.clear();
                        if(response.body().getData().size()>0) {

                            shipstateList.addAll(response.body().getData());
                        }else{
                            StateData sta = new StateData();
                            sta.setName("Select State");
                            shipstateList.add(sta);
                        }
                        shipStateAdapter = new StateAdapter(getContext(),shipstateList);
                        ship_state_value.setAdapter(shipStateAdapter);
                        shipStateAdapter.notifyDataSetChanged();
                        shiptoState= shipstateList.get(0).getName();
                        shiptoStateCode= shipstateList.get(0).getCode();
                    }

                }
                else
                {
                    //Globals.ErrorMessage(CreateContact.this,response.errorBody().toString());
                    Gson gson = new GsonBuilder().create();
                    QuotationResponse mError = new QuotationResponse();
                    try {
                        String s =response.errorBody().string();
                        mError= gson.fromJson(s,QuotationResponse.class);
                        Toast.makeText(getContext(), mError.getError().getMessage().getValue(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        //handle failure to read error
                    }
                    //Toast.makeText(CreateContact.this, msz, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<StateRespose> call, Throwable t) {

                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setDefaults()
     {

        head_title.setText(getResources().getString(R.string.add_order));
        back_press.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }
    private boolean validation(String billingName,String bAddress)
     {
        if(billingName.isEmpty()){
       Globals.showMessage(getActivity(),getResources().getString(R.string.can_not_empty));
       return  false;
        }
        else if(bAddress.isEmpty())
        {
       Globals.showMessage(getActivity(),getResources().getString(R.string.can_not_empty));
       return false;
        }
        return true;

    }

    private void addressCheck(boolean isChecked)
    {
        if(isChecked) {
            ship_block.setVisibility(View.VISIBLE);
        }else{
            ship_block.setVisibility(View.GONE);
        }

    }

    ArrayList<BPAddress> billData = new ArrayList<>();
    ArrayList<BPAddress> shipData = new ArrayList<>();

    private void listFilter(ArrayList<BPAddress> data){

        for (BPAddress st : data){
            if(st.getAddressType().trim().equalsIgnoreCase("bo_BillTo"))
                billData.add(st);
            else if(st.getAddressType().trim().equalsIgnoreCase("bo_ShipTo"))
                shipData.add(st);
        }
        setBranches(billData,shipData);
    }
    private void setBranches(ArrayList<BPAddress> billData,ArrayList<BPAddress> shipData){
        BranchTypeAdapter  billBranchAdapter = new BranchTypeAdapter(getContext(),billData);
        billing_branch_value.setAdapter(billBranchAdapter);
        BranchTypeAdapter  shipBranchAdapter = new BranchTypeAdapter(getContext(),shipData);
        shiping_branch_value.setAdapter(shipBranchAdapter);

        billing_branch_value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
           {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setBillingAddress(billData.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                if(shipData.size()>0)
                setBillingAddress(billData.get(0));
            }
        });

        shiping_branch_value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
           {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setShippingAddress(shipData.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                if(shipData.size()>0)
                setShippingAddress(shipData.get(0));
            }
        });

    }

    private void setBillingAddress(BPAddress bpAddress)
      {
        billing_address_value.setText(bpAddress.getStreet());
        billing_name_value.setText(bpAddress.getAddressName());
        zip_code_value.setText(bpAddress.getZipCode());
        country_value.setSelection(Globals.getCountrypos(Dashboard.countrylist,bpAddress.getUCountry()));

        if(stateList.size()>0)
        state_value.setSelection(Globals.getStatePo(stateList,bpAddress.getUState()));
        billing_district_value.setText(bpAddress.getDistrict());
    }
    private void setShippingAddress(BPAddress bpAddress)
       {
        shipping_address_value.setText(bpAddress.getStreet());
        shipping_name_value.setText(bpAddress.getAddressName());
        zipcode_value2.setText(bpAddress.getZipCode());
        ship_country_value.setSelection(Globals.getCountrypos(Dashboard.countrylist,bpAddress.getUCountry()));

        if(stateList.size()>0)
            state_value.setSelection(Globals.getStatePo(stateList,bpAddress.getUState()));
        shipping_district_value.setText(bpAddress.getDistrict());
    }

}