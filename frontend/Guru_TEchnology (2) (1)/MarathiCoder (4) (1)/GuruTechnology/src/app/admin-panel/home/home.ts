import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgChartsModule } from 'ng2-charts';
import {
  ChartData,
  ChartOptions,
  ChartType,
} from 'chart.js';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, NgChartsModule],
  templateUrl: './home.html',
  styleUrls: ['./home.css'],
})
export class Home {
  // Revenue Chart
  revenueData: ChartData<'bar'> = {
    labels: ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'],
    datasets: [
      {
        data: [5, 4, 6, 7, 5, 4, 6, 5, 4, 6, 7, 5],
        label: 'Last 6 days',
        backgroundColor: '#6f7bf7',
      },
      {
        data: [4, 3, 5, 6, 4, 3, 5, 4, 3, 5, 6, 4],
        label: 'Last Week',
        backgroundColor: '#dadbf7',
      }
    ]
  };

  revenueOptions: ChartOptions = {
    responsive: true,
    plugins: {
      legend: { display: false }
    }
  };

  // Class Time Doughnut
  classTimeData: ChartData<'doughnut'> = {
    labels: ['Afternoon', 'Evening', 'Morning'],
    datasets: [{
      data: [40, 32, 28],
      backgroundColor: ['#6f7bf7', '#aaaaf7', '#d3d3f7']
    }]
  };

  // Payment Line Chart
  paymentData: ChartData<'line'> = {
    labels: ['01', '02', '03', '04', '05', '06'],
    datasets: [
      {
        data: [2100, 2000, 2200, 2300, 2500, 2568],
        label: 'Last 6 days',
        borderColor: '#6f7bf7',
        fill: false
      },
      {
        data: [1800, 1950, 2050, 2150, 2250, 2300],
        label: 'Last Week',
        borderColor: '#d3d3f7',
        fill: false
      }
    ]
  };

  paymentOptions: ChartOptions = {
    responsive: true,
    plugins: {
      legend: { display: false }
    }
  };

  // Enrolled Pie Charts
  enrolledPython: ChartData<'pie'> = {
    labels: ['Python'],
    datasets: [{ data: [85, 15], backgroundColor: ['#6f7bf7', '#eaeaea'] }]
  };
  enrolledJava: ChartData<'pie'> = {
    labels: ['Java'],
    datasets: [{ data: [85, 15], backgroundColor: ['#ffaa33', '#eaeaea'] }]
  };
  enrolledDotNet: ChartData<'pie'> = {
    labels: ['.NET'],
    datasets: [{ data: [92, 8], backgroundColor: ['#33cccc', '#eaeaea'] }]
  };

  // 👉 Added this for bubble canvas charts (overlapping circles)
  bubbleChartOptions: ChartOptions = {
    responsive: true,
    plugins: {
      legend: {
        display: false
      },
      tooltip: {
        enabled: false
      }
    }
  };

  // Upcoming Courses List
  courses = [
    { name: 'Java', price: 'IDR 45.000' },
    { name: '.NET', price: 'IDR 75.000' },
    { name: 'Java', price: 'IDR 45.000' },
    { name: 'Java', price: 'IDR 45.000' },
  ];
}