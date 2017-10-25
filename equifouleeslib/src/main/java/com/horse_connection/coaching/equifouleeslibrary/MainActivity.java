package com.horse_connection.coaching.equifouleeslibrary;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Hashtable;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private final Hashtable<String, Integer> htHauteurs = new Hashtable<>();
    private final Hashtable<String, Integer> htLargeurs = new Hashtable<>();
    private final Hashtable<String, Integer> distAppelMin = new Hashtable<>();
    private Context ctx;
    private EquiFouleesParams params;
    private boolean boolAskMe = Boolean.FALSE;
    private boolean initDefault = Boolean.TRUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = getApplicationContext();
        String[] tmp = {"C", "C"};

        for (String s : Constants.Hauteurs) {
            String[] array = s.split("/");
            String sKey;
            int sValue;
            if (array.length > 1) {
                sKey = array[0];
                sValue = Integer.parseInt(array[1]);
                htHauteurs.put(sKey, sValue);
            }
        }

        for (String s : Constants.Largeurs) {
            String[] array = s.split("/");
            String sKey;
            int sValue;
            if (array.length > 1) {
                sKey = array[0];
                sValue = Integer.parseInt(array[1]);
                htLargeurs.put(sKey, sValue);
            }
        }

        for (String s : Constants.AppelMin) {
            String[] array = s.split("/");
            String sKey;
            int sValue;
            if (array.length > 1) {
                sKey = array[0];
                sValue = Integer.parseInt(array[1]);
                distAppelMin.put(sKey, sValue);
            }
        }

        params = new EquiFouleesParams(String.valueOf(Constants.foulC), tmp, new String[]{String.valueOf(htHauteurs.get("C")), String.valueOf(htHauteurs.get("C"))}, new String[]{String.valueOf(htLargeurs.get("C")), String.valueOf(htLargeurs.get("C"))});

        setContentView(R.layout.activity_main);

        ((EditText) findViewById(R.id.editFC)).setText(String.valueOf(Constants.foulC));
        ((EditText) findViewById(R.id.editFPA)).setText(String.valueOf(Constants.foulPA));
        ((EditText) findViewById(R.id.editFPB)).setText(String.valueOf(Constants.foulPB));
        ((EditText) findViewById(R.id.editFPC)).setText(String.valueOf(Constants.foulPC));
        ((EditText) findViewById(R.id.editFPD)).setText(String.valueOf(Constants.foulPD));

        ((EditText) findViewById(R.id.editHautObst1)).setText(String.valueOf(htHauteurs.get(params.getpTypeCalc())));
        ((EditText) findViewById(R.id.editHautObst2)).setText(String.valueOf(htHauteurs.get(params.getpTypeCalc())));
        ((EditText) findViewById(R.id.editLargObst1)).setText(String.valueOf(htLargeurs.get(params.getpTypeCalc())));
        ((EditText) findViewById(R.id.editLargObst2)).setText(String.valueOf(htLargeurs.get(params.getpTypeCalc())));

        Button btnCalc = (Button) findViewById(R.id.btnCalc);
        btnCalc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // On commence par cacher le clavier virtuel s'il est encore visible
                final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                switch (params.getpTypeCalc()) {
                    case "C":
                        params.setpLgFoul(((EditText) findViewById(R.id.editFC)).getText().toString());
                        break;
                    case "PA":
                        params.setpLgFoul(((EditText) findViewById(R.id.editFPA)).getText().toString());
                        break;
                    case "PB":
                        params.setpLgFoul(((EditText) findViewById(R.id.editFPB)).getText().toString());
                        break;
                    case "PC":
                        params.setpLgFoul(((EditText) findViewById(R.id.editFPC)).getText().toString());
                        break;
                    case "PD":
                        params.setpLgFoul(((EditText) findViewById(R.id.editFPD)).getText().toString());
                        break;
                }
                CheckBox cbDispoBR = (CheckBox) findViewById(R.id.cbDispoBR);
                params.setpReglage(cbDispoBR.isChecked());
                params.setpHautObst(new String[]{((EditText) findViewById(R.id.editHautObst1)).getText().toString(), ((EditText) findViewById(R.id.editHautObst2)).getText().toString()});
                params.setpLargObst(new String[]{((EditText) findViewById(R.id.editLargObst1)).getText().toString(), ((EditText) findViewById(R.id.editLargObst2)).getText().toString()});


                // tester que la hauteur du second obstacle n'est pas < au premier
                int hautObst1 = Integer.valueOf(params.getpHautObst()[0]);
                int hautObst2 = Integer.valueOf(params.getpHautObst()[1]);
                int largObst1 = Integer.valueOf(params.getpLargObst()[0]);
                int largObst2 = Integer.valueOf(params.getpLargObst()[1]);
                if (hautObst1 > hautObst2) {
                    Toast toast = Toast.makeText(ctx, "ATTENTION !! le dispositif demandé est DANGEREUX !!!", Toast.LENGTH_SHORT);
                    toast.show();
                }

                // Calcul de la distance de la barre de réglage si nécessaire
                double distBR = 0.0;
                if (params.ispReglage()) {
                    if (Objects.equals(params.getpEntree(), "T")) {
                        distBR = Math.round(((Double.parseDouble(params.getpLgFoul()) * Constants.coefBR) / 2) / 10) * 10;
                    } else {
                        distBR = Math.round(((Double.parseDouble(params.getpLgFoul())) * Constants.coefBR) / 10) * 10;
                    }
                }

                // Déterminer la longueur à ajouter
                double distAjout;
                if (hautObst1 <= htHauteurs.get(params.getpTypeCalc())) {
                    // Distance Travail
                    distAjout = (distAppelMin.get(params.getpTypeCalc()) * 2);
                } else {
                    // Distance Compétition
                    distAjout = distAppelMin.get(params.getpTypeCalc()) * 4;
                }
                distAjout += (hautObst2 * 1.9);
                if (Objects.equals(params.getpProfil0(), "OC")) {
                    // On soustrait la moitié de la largeur si le premier obstacle est un Oxer carré
                    distAjout -= (largObst1 / 2);
                }
                if (Objects.equals(params.getpProfil1(), "OC")) {
                    // On soustrait la moitié de la largeur si le second obstacle est un Oxer carré
                    distAjout -= (largObst2 / 2);
                }
                if (Objects.equals(params.getpProfil1(), "OM")) {
                    // On soustrait la moitié de la largeur si le second obstacle est un Oxer montant
                    distAjout -= largObst2;
                }

                // Si entrée au trot, on enlève la valeur d'une distance d'appel mini
                if (Objects.equals(params.getpEntree(), "T")) {
                    distAjout -= distAppelMin.get(params.getpTypeCalc());
                }

                double[] distFinale = {0.0, 0.0, 0.0, 0.0, 0.0};
                distFinale[0] = Math.round(distAjout / 10) * 10; // saut de puce
                distFinale[1] = Math.round((Double.parseDouble(params.getpLgFoul()) + distAjout) / 10) * 10; // 1 foulée
                distFinale[2] = Math.round(((Double.parseDouble(params.getpLgFoul()) * 2) + distAjout) / 10) * 10; // 2 foulées
                distFinale[3] = Math.round(((Double.parseDouble(params.getpLgFoul()) * 3) + distAjout) / 10) * 10; // 3 foulées
                distFinale[4] = Math.round(((Double.parseDouble(params.getpLgFoul()) * 4) + distAjout) / 10) * 10; // 4 foulées

                ((TextView) findViewById(R.id.tvSPRes)).setText(String.valueOf(distFinale[0] / 100));
                ((TextView) findViewById(R.id.tv1FRes)).setText(String.valueOf(distFinale[1] / 100));
                ((TextView) findViewById(R.id.tv2FRes)).setText(String.valueOf(distFinale[2] / 100));
                ((TextView) findViewById(R.id.tv3FRes)).setText(String.valueOf(distFinale[3] / 100));
                ((TextView) findViewById(R.id.tv4FRes)).setText(String.valueOf(distFinale[4] / 100));
                ((TextView) findViewById(R.id.tvBRRes)).setText(String.valueOf(distBR / 100));

                (findViewById(R.id.clResultat)).setVisibility(View.VISIBLE);

                if (BuildConfig.DEBUG) {
                    Toast toast = Toast.makeText(ctx, "Calcul effectué", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

    }

    public void onRBTabFouleesClicked(View v) {
        if ((findViewById(R.id.clResultat)).getVisibility() == View.VISIBLE) {
            (findViewById(R.id.clResultat)).setVisibility(View.INVISIBLE);
        }

        // Traitement des boutons Foulées
        RadioButton rbC = (RadioButton) findViewById(R.id.rbC);
        RadioButton rbPA = (RadioButton) findViewById(R.id.rbPA);
        RadioButton rbPB = (RadioButton) findViewById(R.id.rbPB);
        RadioButton rbPC = (RadioButton) findViewById(R.id.rbPC);
        RadioButton rbPD = (RadioButton) findViewById(R.id.rbPD);

        int id = v.getId();
        if (id == R.id.rbC) {
            params.setpTypeCalc("C");
            (findViewById(R.id.editFC)).requestFocus();
            rbPA.setChecked(false);
            rbPB.setChecked(false);
            rbPC.setChecked(false);
            rbPD.setChecked(false);
        } else if (id == R.id.rbPA) {
            params.setpTypeCalc("PA");
            (findViewById(R.id.editFPA)).requestFocus();
            rbC.setChecked(false);
            rbPB.setChecked(false);
            rbPC.setChecked(false);
            rbPD.setChecked(false);
        } else if (id == R.id.rbPB) {
            params.setpTypeCalc("PB");
            (findViewById(R.id.editFPB)).requestFocus();
            rbC.setChecked(false);
            rbPA.setChecked(false);
            rbPC.setChecked(false);
            rbPD.setChecked(false);
        } else if (id == R.id.rbPC) {
            params.setpTypeCalc("PC");
            (findViewById(R.id.editFPC)).requestFocus();
            rbC.setChecked(false);
            rbPA.setChecked(false);
            rbPB.setChecked(false);
            rbPD.setChecked(false);
        } else if (id == R.id.rbPD) {
            params.setpTypeCalc("PD");
            (findViewById(R.id.editFPD)).requestFocus();
            rbC.setChecked(false);
            rbPA.setChecked(false);
            rbPB.setChecked(false);
            rbPC.setChecked(false);
        }

        params.setpHautObst(new String[]{String.valueOf(htHauteurs.get(params.getpTypeCalc())), String.valueOf(htHauteurs.get(params.getpTypeCalc()))});
        params.setpLargObst(new String[]{String.valueOf(htLargeurs.get(params.getpTypeCalc())), String.valueOf(htLargeurs.get(params.getpTypeCalc()))});

        // alertDialogPerso
        if (!boolAskMe) {
            final Dialog alertDialog = new Dialog(MainActivity.this);
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.setContentView(R.layout.alert_dialog);

            Button btnOK = alertDialog.findViewById(R.id.btnOK);
            Button btnKO = alertDialog.findViewById(R.id.btnKO);
            final CheckBox cbAskMe = alertDialog.findViewById(R.id.cbAskMe);

            // OK -> on réinitialise avec les valeurs par défaut
            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cbAskMe.isChecked()) {
                        boolAskMe = true;
                    }
                    initDefault = true;
                    alertDialog.dismiss();
                    if (BuildConfig.DEBUG) {
                        Toast.makeText(getApplicationContext(), "OK !!!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            btnKO.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cbAskMe.isChecked()) {
                        boolAskMe = true;
                    }
                    initDefault = false;
                    alertDialog.dismiss();
                    if (BuildConfig.DEBUG) {
                        Toast.makeText(getApplicationContext(), "KO !!!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            alertDialog.show();
        }

        // Réinitialiser les hauteurs/largeurs par défaut
        if (initDefault) {
            ((EditText) findViewById(R.id.editHautObst1)).setText(String.valueOf(htHauteurs.get(params.getpTypeCalc())));
            ((EditText) findViewById(R.id.editHautObst2)).setText(String.valueOf(htHauteurs.get(params.getpTypeCalc())));
            ((EditText) findViewById(R.id.editLargObst1)).setText(String.valueOf(htLargeurs.get(params.getpTypeCalc())));
            ((EditText) findViewById(R.id.editLargObst2)).setText(String.valueOf(htLargeurs.get(params.getpTypeCalc())));
        }
    }

    public void onRBTabProfilClicked(View v) {
        if ((findViewById(R.id.clResultat)).getVisibility() == View.VISIBLE) {
            (findViewById(R.id.clResultat)).setVisibility(View.INVISIBLE);
        }

        // Traitement des boutons Profil
        int id = v.getId();
        if (id == R.id.rbDispoObs11) {
            params.setpProfil0("V");
            (findViewById(R.id.editHautObst1)).requestFocus();
            ((RadioButton) findViewById(R.id.rbDispoObs12)).setChecked(false);
            ((RadioButton) findViewById(R.id.rbDispoObs13)).setChecked(false);
            (findViewById(R.id.tvLargObst1)).setVisibility(View.INVISIBLE);
            (findViewById(R.id.editLargObst1)).setVisibility(View.INVISIBLE);
        } else if (id == R.id.rbDispoObs12) {
            params.setpProfil0("OC");
            (findViewById(R.id.editHautObst1)).requestFocus();
            ((RadioButton) findViewById(R.id.rbDispoObs11)).setChecked(false);
            ((RadioButton) findViewById(R.id.rbDispoObs13)).setChecked(false);
            (findViewById(R.id.tvLargObst1)).setVisibility(View.VISIBLE);
            (findViewById(R.id.editLargObst1)).setVisibility(View.VISIBLE);
        } else if (id == R.id.rbDispoObs13) {
            params.setpProfil0("OM");
            (findViewById(R.id.editHautObst1)).requestFocus();
            ((RadioButton) findViewById(R.id.rbDispoObs11)).setChecked(false);
            ((RadioButton) findViewById(R.id.rbDispoObs12)).setChecked(false);
            (findViewById(R.id.tvLargObst1)).setVisibility(View.VISIBLE);
            (findViewById(R.id.editLargObst1)).setVisibility(View.VISIBLE);
        } else if (id == R.id.rbDispoObs21) {
            params.setpProfil1("V");
            (findViewById(R.id.editHautObst2)).requestFocus();
            ((RadioButton) findViewById(R.id.rbDispoObs22)).setChecked(false);
            ((RadioButton) findViewById(R.id.rbDispoObs23)).setChecked(false);
            (findViewById(R.id.tvLargObst2)).setVisibility(View.INVISIBLE);
            (findViewById(R.id.editLargObst2)).setVisibility(View.INVISIBLE);
        } else if (id == R.id.rbDispoObs22) {
            params.setpProfil1("OC");
            (findViewById(R.id.editHautObst2)).requestFocus();
            ((RadioButton) findViewById(R.id.rbDispoObs21)).setChecked(false);
            ((RadioButton) findViewById(R.id.rbDispoObs23)).setChecked(false);
            (findViewById(R.id.tvLargObst2)).setVisibility(View.VISIBLE);
            (findViewById(R.id.editLargObst2)).setVisibility(View.VISIBLE);
        } else if (id == R.id.rbDispoObs23) {
            params.setpProfil1("OM");
            (findViewById(R.id.editHautObst2)).requestFocus();
            ((RadioButton) findViewById(R.id.rbDispoObs21)).setChecked(false);
            ((RadioButton) findViewById(R.id.rbDispoObs22)).setChecked(false);
            (findViewById(R.id.tvLargObst2)).setVisibility(View.VISIBLE);
            (findViewById(R.id.editLargObst2)).setVisibility(View.VISIBLE);
        } else if (id == R.id.rbDispoET) {
            params.setpEntree("T");
            ((RadioButton) findViewById(R.id.rbDispoEG)).setChecked(false);
        } else if (id == R.id.rbDispoEG) {
            params.setpEntree("G");
            ((RadioButton) findViewById(R.id.rbDispoET)).setChecked(false);
        }

    }
}
