package com.example.petapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.petapp.R;

public class Fragment2 extends Fragment {

    private TextView txtPetName, txtPetRace, txtPetSpecies, txtPetSex, txtPetColor;
    private TextView txtPetCity, txtPetState, txtPetNeighborhood, txtPetAddress;
    private TextView txtPetEmail, txtPetFather, txtPetMother, txtPetBirthplace;
    private TextView txtPetDescription, txtPetCep, txtPetPhone, txtPetCell, txtPetBirth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2, container, false);

        // Inicializar TextViews
        txtPetName = view.findViewById(R.id.txt_pet_name);
        txtPetRace = view.findViewById(R.id.txt_pet_race);
        txtPetSpecies = view.findViewById(R.id.txt_pet_species);
        txtPetSex = view.findViewById(R.id.txt_pet_sex);
        txtPetColor = view.findViewById(R.id.txt_pet_color);
        txtPetCity = view.findViewById(R.id.txt_pet_city);
        txtPetState = view.findViewById(R.id.txt_pet_state);
        txtPetNeighborhood = view.findViewById(R.id.txt_pet_neighborhood);
        txtPetAddress = view.findViewById(R.id.txt_pet_address);
        txtPetEmail = view.findViewById(R.id.txt_pet_email);
        txtPetFather = view.findViewById(R.id.txt_pet_father);
        txtPetMother = view.findViewById(R.id.txt_pet_mother);
        txtPetBirthplace = view.findViewById(R.id.txt_pet_birthplace);
        txtPetDescription = view.findViewById(R.id.txt_pet_description);
        txtPetCep = view.findViewById(R.id.txt_pet_cep);
        txtPetPhone = view.findViewById(R.id.txt_pet_phone);
        txtPetCell = view.findViewById(R.id.txt_pet_cell);
        txtPetBirth = view.findViewById(R.id.txt_pet_birth);

        // Receber dados do bundle
        Bundle arguments = getArguments();
        if (arguments != null) {
            txtPetName.setText(arguments.getString("pet_name", ""));
            txtPetRace.setText(arguments.getString("pet_race", ""));
            txtPetSpecies.setText(arguments.getString("pet_species", ""));
            txtPetSex.setText(arguments.getString("pet_sex", ""));
            txtPetColor.setText(arguments.getString("pet_color", ""));
            txtPetCity.setText(arguments.getString("pet_city", ""));
            txtPetState.setText(arguments.getString("pet_state", ""));
            txtPetNeighborhood.setText(arguments.getString("pet_neighborhood", ""));
            txtPetAddress.setText(arguments.getString("pet_address", ""));
            txtPetEmail.setText(arguments.getString("pet_email", ""));
            txtPetFather.setText(arguments.getString("pet_father", ""));
            txtPetMother.setText(arguments.getString("pet_mother", ""));
            txtPetBirthplace.setText(arguments.getString("pet_birthplace", ""));
            txtPetDescription.setText(arguments.getString("pet_description", ""));
            txtPetCep.setText(formatCep(arguments.getString("pet_cep", "")));
            txtPetPhone.setText(formatPhone(arguments.getString("pet_phone", "")));
            txtPetCell.setText(formatCellPhone(arguments.getString("pet_cell", "")));
            txtPetBirth.setText(formatBirthDate(arguments.getString("pet_birth", "")));
        }

        return view;
    }

    private String formatCep(String cep) {
        if (cep == null || cep.isEmpty() || cep.equals("0")) return "";
        if (cep.length() == 8) {
            return cep.substring(0, 5) + "-" + cep.substring(5);
        }
        return cep;
    }

    private String formatPhone(String phone) {
        if (phone == null || phone.isEmpty() || phone.equals("0")) return "";
        if (phone.length() == 10) {
            return "(" + phone.substring(0, 2) + ") " + phone.substring(2, 6) + "-" + phone.substring(6);
        }
        return phone;
    }

    private String formatCellPhone(String cell) {
        if (cell == null || cell.isEmpty() || cell.equals("0")) return "";
        if (cell.length() == 11) {
            return "(" + cell.substring(0, 2) + ") " + cell.substring(2, 7) + "-" + cell.substring(7);
        }
        return cell;
    }

    private String formatBirthDate(String birth) {
        if (birth == null || birth.isEmpty() || birth.equals("0")) return "";
        if (birth.length() == 8) {
            return birth.substring(0, 2) + "/" + birth.substring(2, 4) + "/" + birth.substring(4);
        }
        return birth;
    }
}