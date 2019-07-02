## Curvy-Progress

I was looking for a curvy ProgressBar/SeekBar, the elements I found didn't satisfy me, so I did my own.

## Screenshot

<p align="center">
  <b>Final Result</b><br>
  <img src="https://github.com/axel0070/Curvy-Progress/blob/master/gif.gif">
</p>

## How it work ?

I extended View and I used android.graphics.Path to build the curve, actually there are draw using a sinus function. The pulsation, frequency and period change to get a random aspect. 