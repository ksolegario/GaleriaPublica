package olegario.kalebe.galeriapublica;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

//mostrar ao Adapter como verificar se dois itens do tipo ImageData são iguais
public class ImageDataComparator extends DiffUtil.ItemCallback<ImageData>{
    //recebe dois objetos do tipo ImageData e retorna true caso eles sejam iguais e false caso contrário
    @Override
    public boolean areItemsTheSame(@NonNull ImageData oldItem, @NonNull ImageData newItem) {
        // Id is unique.
        return oldItem.uri.equals(newItem.uri);
    }
    //recebe dois objetos do tipo ImageData e retorna true caso eles tenham o mesmo conteúdo e false caso contrário
    @Override
    public boolean areContentsTheSame(@NonNull ImageData oldItem, @NonNull ImageData newItem) {
        return oldItem.uri.equals(newItem.uri);
    }
}