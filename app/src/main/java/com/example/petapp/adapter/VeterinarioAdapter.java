package com.example.petapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petapp.R;

import java.util.List;

public class VeterinarioAdapter extends BaseAdapter {

    private Context context;
    private List<VeterinarioModel> veterinarios;
    private LayoutInflater inflater;

    public VeterinarioAdapter(Context context, List<VeterinarioModel> veterinarios) {
        this.context = context;
        this.veterinarios = veterinarios;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return veterinarios != null ? veterinarios.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return veterinarios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_veterinario, parent, false);
            holder = new ViewHolder();
            holder.tvNome = convertView.findViewById(R.id.tv_nome);
            holder.tvEndereco = convertView.findViewById(R.id.tv_endereco);
            holder.tvTelefone = convertView.findViewById(R.id.tv_telefone);
            holder.tvHorario = convertView.findViewById(R.id.tv_horario);
            holder.tvTipo = convertView.findViewById(R.id.tv_tipo);
            holder.tvAvaliacao = convertView.findViewById(R.id.tv_avaliacao);
            holder.tvEspecialidades = convertView.findViewById(R.id.tv_especialidades);
            holder.ivEmergencia = convertView.findViewById(R.id.iv_emergencia);
            holder.ivTelefone = convertView.findViewById(R.id.iv_telefone);
            holder.ivMaps = convertView.findViewById(R.id.iv_maps);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        VeterinarioModel veterinario = veterinarios.get(position);

        holder.tvNome.setText(veterinario.getNome());
        holder.tvEndereco.setText(veterinario.getEndereco());
        holder.tvTelefone.setText(veterinario.getTelefone());
        holder.tvHorario.setText(veterinario.getHorarioFuncionamento());
        holder.tvTipo.setText(veterinario.getTipo());
        holder.tvAvaliacao.setText(String.format("⭐ %.1f", veterinario.getAvaliacao()));
        holder.tvEspecialidades.setText(veterinario.getEspecialidades());

        // Ícone de emergência
        if (veterinario.isAtendeEmergencia()) {
            holder.ivEmergencia.setVisibility(View.VISIBLE);
        } else {
            holder.ivEmergencia.setVisibility(View.GONE);
        }

        // Click para ligar
        holder.ivTelefone.setOnClickListener(v -> {
            if (veterinario.getTelefone() != null && !veterinario.getTelefone().isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + veterinario.getTelefone()));
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Telefone não disponível", Toast.LENGTH_SHORT).show();
            }
        });

        // Click para abrir no Maps usando o link direto
        holder.ivMaps.setOnClickListener(v -> {
            if (veterinario.getLinkMaps() != null && !veterinario.getLinkMaps().isEmpty()) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(veterinario.getLinkMaps()));
                    intent.setPackage("com.google.android.apps.maps");

                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(intent);
                    } else {
                        // Fallback para navegador se o Google Maps não estiver instalado
                        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(veterinario.getLinkMaps()));
                        context.startActivity(webIntent);
                    }
                } catch (Exception e) {
                    Toast.makeText(context, "Erro ao abrir localização", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Localização não disponível", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView tvNome;
        TextView tvEndereco;
        TextView tvTelefone;
        TextView tvHorario;
        TextView tvTipo;
        TextView tvAvaliacao;
        TextView tvEspecialidades;
        ImageView ivEmergencia;
        ImageView ivTelefone;
        ImageView ivMaps;
    }

    public void updateData(List<VeterinarioModel> novaLista) {
        this.veterinarios = novaLista;
        notifyDataSetChanged();
    }
}