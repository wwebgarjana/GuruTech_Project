import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-trainer-live-classes',
  standalone: true,
  imports: [RouterModule], // ✅ Add this line
  templateUrl: './live-classes.html',
  styleUrls: ['./live-classes.css']
})
export class LiveClasses {}
