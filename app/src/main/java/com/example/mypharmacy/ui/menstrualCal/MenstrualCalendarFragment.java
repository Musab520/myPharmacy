package com.example.mypharmacy.ui.menstrualCal;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypharmacy.R;
import com.example.mypharmacy.data.local.entities.Menstruation;
import com.example.mypharmacy.data.local.entities.Person;
import com.example.mypharmacy.data.local.repositories.MenstruationRepository;
import com.example.mypharmacy.data.local.repositories.impl.MenstruationRepositoryImpl;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.concurrent.Executor;

public class MenstrualCalendarFragment extends Fragment implements CalendarAdapter.OnItemListener {
    //todo check if start dates are available if not send intent to survey
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private Button nextMonthButton;
    private Button previousMonthButton;
    private LocalDate selectedDate;

    private MutableLiveData<Menstruation> menstruation = new MutableLiveData<>();

    public LiveData<Menstruation> getData() {
        return menstruation;
    }

    public MenstrualCalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_menstrual_calendar, container, false);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getMenstruation();
        this.getData().observe(getViewLifecycleOwner(), new Observer<Menstruation>() {
            @Override
            public void onChanged(Menstruation menstruation) {
                if (menstruation == null) {
                    Fragment fragment = new MenstrualCycleSurvey();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else {
                    initWidgets(view);
                    setListeners();
                    SetMonthView();
                }
            }
        });

    }

    private void getMenstruation() {
        MenstruationRepository menstruationRepository = new MenstruationRepositoryImpl(this.getContext());
        new Thread(new Runnable() {
            @Override
            public void run() {
                Menstruation menstruationTemp = menstruationRepository.getMenstruation();
                menstruation.postValue(menstruationTemp);
            }
        }).start();


    }


    private void setListeners() {
        previousMonthButton.setOnClickListener(e -> {
            selectedDate = selectedDate.minusMonths(1);
            SetMonthView();
        });

        nextMonthButton.setOnClickListener(e -> {
            selectedDate = selectedDate.plusMonths(1);
            SetMonthView();
        });
    }

    private void SetMonthView() {
        monthYearText.setText(MonthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = DaysInMonthArray(selectedDate);
        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext().getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);

    }

    private ArrayList<String> DaysInMonthArray(LocalDate selectedDate) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(selectedDate);
        int daysInMonth = yearMonth.lengthOfMonth();
        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for (int i = 1; i <= 42; i++) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("");
            } else {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return daysInMonthArray;
    }

    private String MonthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }


    private void initWidgets(View view) {
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);
        monthYearText = view.findViewById(R.id.monthYearTextview);
        selectedDate = LocalDate.now();
        previousMonthButton = view.findViewById(R.id.PreviousMonthButton);
        nextMonthButton = view.findViewById(R.id.NextMonthButton);

    }

    @Override
    public void OnItemClick(int position, String dayText) {
        if (!dayText.equals("")) {
            String message = "Selected Date " + dayText + " " + MonthYearFromDate(selectedDate);
            Toast.makeText(this.getContext(), message, Toast.LENGTH_LONG).show();
        }
    }
}
